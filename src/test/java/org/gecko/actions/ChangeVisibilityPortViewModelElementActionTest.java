package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.gecko.exceptions.ModelException;
import org.gecko.model.Visibility;
import org.gecko.util.TestHelper;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChangeVisibilityPortViewModelElementActionTest {
    private PortViewModel port;
    private ActionManager actionManager;
    private ActionFactory actionFactory;

    @BeforeEach
    void setUp() throws ModelException {
        GeckoViewModel geckoViewModel = TestHelper.createGeckoViewModel();
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        SystemViewModel rootSystemViewModel =
            viewModelFactory.createSystemViewModelFrom(geckoViewModel.getGeckoModel().getRoot());

        SystemViewModel systemViewModel1 = viewModelFactory.createSystemViewModelIn(rootSystemViewModel);
        port = viewModelFactory.createPortViewModelIn(systemViewModel1);
    }

    @Test
    void run() {
        Action changeKindAction = actionFactory.createChangeVisibilityPortViewModelAction(port, Visibility.OUTPUT);
        actionManager.run(changeKindAction);
        assertEquals(Visibility.OUTPUT, port.getVisibility());
    }

    @Test
    void runSameVisibility() {
        Action changeKindAction = actionFactory.createChangeVisibilityPortViewModelAction(port, port.getVisibility());
        actionManager.run(changeKindAction);
        assertEquals(Visibility.INPUT, port.getVisibility());
    }

    @Test
    void getUndoAction() {
        Action changeVisibilityPortViewModelAction =
            actionFactory.createChangeVisibilityPortViewModelAction(port, Visibility.OUTPUT);
        Visibility beforeChangeVisibility = port.getVisibility();
        actionManager.run(changeVisibilityPortViewModelAction);
        actionManager.undo();
        assertEquals(beforeChangeVisibility, port.getVisibility());
        assertEquals(beforeChangeVisibility, port.getTarget().getVisibility());
    }
}
