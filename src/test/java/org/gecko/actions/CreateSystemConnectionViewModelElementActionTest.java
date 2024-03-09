package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.gecko.exceptions.ModelException;
import org.gecko.model.Visibility;
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
    static void setUp() throws ModelException {
        GeckoViewModel geckoViewModel = TestHelper.createGeckoViewModel();
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        parent = viewModelFactory.createSystemViewModelFrom(geckoViewModel.getGeckoModel().getRoot());
        SystemViewModel system1 = viewModelFactory.createSystemViewModelIn(parent);
        SystemViewModel system2 = viewModelFactory.createSystemViewModelIn(parent);
        port1 = viewModelFactory.createPortViewModelIn(system1);
        port2 = viewModelFactory.createPortViewModelIn(system2);
    }

    @Test
    void run() {
        Action createSystemConnectionAction =
            actionFactory.createCreateSystemConnectionViewModelElementAction(port1, port2);
        actionManager.run(createSystemConnectionAction);
        assertEquals(0, parent.getTarget().getConnections().size());

        port1.setVisibility(Visibility.OUTPUT);
        assertDoesNotThrow(() -> port1.updateTarget());
        port2.setVisibility(Visibility.INPUT);
        assertDoesNotThrow(() -> port2.updateTarget());

        actionManager.run(createSystemConnectionAction);
        assertEquals(1, parent.getTarget().getConnections().size());
    }

    @Test
    void getUndoAction() {
        assertEquals(1, parent.getTarget().getConnections().size());
        actionManager.undo();
        assertEquals(0, parent.getTarget().getConnections().size());
    }
}