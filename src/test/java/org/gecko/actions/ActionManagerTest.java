package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.geometry.Point2D;
import org.gecko.exceptions.ModelException;
import org.gecko.util.TestHelper;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ActionManagerTest {
    private ActionManager actionManager;
    private ActionFactory actionFactory;
    private SystemViewModel systemViewModel;

    @BeforeEach
    void setUp() throws ModelException {
        GeckoViewModel geckoViewModel = TestHelper.createGeckoViewModel();
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        systemViewModel = viewModelFactory.createSystemViewModelFrom(geckoViewModel.getGeckoModel().getRoot());
        geckoViewModel.switchEditor(systemViewModel, true);
    }

    @Test
    void testRedo() {
        Action createStateAction = actionFactory.createCreateStateViewModelElementAction(new Point2D(100, 100));
        actionManager.run(createStateAction);
        assertEquals(1, systemViewModel.getTarget().getAutomaton().getStates().size());
        actionManager.undo();
        assertEquals(0, systemViewModel.getTarget().getAutomaton().getStates().size());
        actionManager.undo();
        actionManager.redo();
        assertEquals(1, systemViewModel.getTarget().getAutomaton().getStates().size());
        actionManager.redo();
    }
}
