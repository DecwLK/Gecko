package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.gecko.util.TestHelper;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CreateSystemConnectionViewModelElementActionTest {

    private static ActionManager actionManager;
    private static ActionFactory actionFactory;
    private static SystemViewModel parent;
    private static PortViewModel port1;
    private static PortViewModel port2;

    @BeforeAll
    static void setUp() {
        GeckoViewModel geckoViewModel = TestHelper.createGeckoViewModel();
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        parent = viewModelFactory.createSystemViewModelFrom(geckoViewModel.getGeckoModel().getRoot());
        port1 = viewModelFactory.createPortViewModelIn(parent);
        port2 = viewModelFactory.createPortViewModelIn(parent);
    }

    @Test
    void run() {
        Action createSystemConnectionAction =
            actionFactory.createCreateSystemConnectionViewModelElementAction(port1, port2);
        actionManager.run(createSystemConnectionAction);
        assertEquals(1, parent.getTarget().getConnections().size());
        //TODO detect deletion from viewModel
    }

    @Test
    void getUndoAction() {
        actionManager.undo();
        assertEquals(0, parent.getTarget().getConnections().size());
    }
}