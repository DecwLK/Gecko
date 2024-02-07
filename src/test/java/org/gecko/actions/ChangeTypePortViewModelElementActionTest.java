package org.gecko.actions;

import org.gecko.util.TestHelper;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChangeTypePortViewModelElementActionTest {

    private PortViewModel port;
    private ActionManager actionManager;
    private ActionFactory actionFactory;

    @BeforeEach
    void setUp() {
        GeckoViewModel geckoViewModel = TestHelper.createGeckoViewModel();
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        SystemViewModel rootSystemViewModel =
            viewModelFactory.createSystemViewModelFrom(geckoViewModel.getGeckoModel().getRoot());
        port = viewModelFactory.createPortViewModelIn(rootSystemViewModel);
    }

    @Test
    void run() {
        Action changeTypeAction = actionFactory.createChangeTypePortViewModelElementAction(port, "newType");
        actionManager.run(changeTypeAction);
        assertEquals("newType", port.getType());
        assertEquals("newType", port.getTarget().getType());
    }

    @Test
    void getUndoAction() {
        Action changeTypeAction = actionFactory.createChangeTypePortViewModelElementAction(port, "newType");
        String beforeChangeType = port.getType();
        actionManager.run(changeTypeAction);
        actionManager.undo();
        assertEquals(beforeChangeType, port.getType());
        assertEquals(beforeChangeType, port.getTarget().getType());
    }
}