package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.gecko.exceptions.ModelException;
import org.gecko.model.Visibility;
import org.gecko.util.TestHelper;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ViewSwitchActionTest {
    private SystemViewModel systemViewModel;
    private SystemViewModel rootSystemViewModel;
    private ActionManager actionManager;
    private ActionFactory actionFactory;
    private GeckoViewModel geckoViewModel;

    @BeforeEach
    void setUp() throws ModelException {
        geckoViewModel = TestHelper.createGeckoViewModel();
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        rootSystemViewModel = viewModelFactory.createSystemViewModelFrom(geckoViewModel.getGeckoModel().getRoot());

        systemViewModel = viewModelFactory.createSystemViewModelIn(rootSystemViewModel);
    }

    @Test
    void switchAutomatonView() {
        Action switchViewAction = actionFactory.createViewSwitchAction(systemViewModel, true);
        actionManager.run(switchViewAction);
        assertEquals(geckoViewModel.getCurrentEditor().getCurrentSystem(), systemViewModel);
        assertTrue(geckoViewModel.getCurrentEditor().isAutomatonEditor());
    }

    @Test
    void switchSystemView() {
        Action switchViewAction = actionFactory.createViewSwitchAction(systemViewModel, false);
        actionManager.run(switchViewAction);
        assertEquals(geckoViewModel.getCurrentEditor().getCurrentSystem(), systemViewModel);
        assertFalse(geckoViewModel.getCurrentEditor().isAutomatonEditor());
    }

    @Test
    void switchToInvalidView() {
        Action switchViewAction = actionFactory.createViewSwitchAction(systemViewModel, true);
        actionManager.run(switchViewAction);
        Action switchToInvalidViewAction = actionFactory.createViewSwitchAction(null, false);
        actionManager.run(switchToInvalidViewAction);
        assertEquals(geckoViewModel.getCurrentEditor().getCurrentSystem(), systemViewModel);
        assertTrue(geckoViewModel.getCurrentEditor().isAutomatonEditor());
    }

    @Test
    void getUndoAction() {
        actionManager.run(actionFactory.createViewSwitchAction(rootSystemViewModel, true));
        Action changeViewSwitchAction = actionFactory.createViewSwitchAction(systemViewModel, true);
        actionManager.run(changeViewSwitchAction);
        actionManager.undo();
        assertEquals(geckoViewModel.getCurrentEditor().getCurrentSystem(), rootSystemViewModel);
    }
}
