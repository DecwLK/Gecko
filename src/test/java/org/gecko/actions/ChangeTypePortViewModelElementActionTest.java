package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.gecko.model.GeckoModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChangeTypePortViewModelElementActionTest {

    private PortViewModel port;
    private ActionManager actionManager;
    private ActionFactory actionFactory;

    @BeforeEach
    void setUp() {
        GeckoModel geckoModel = new GeckoModel();
        GeckoViewModel geckoViewModel = new GeckoViewModel(geckoModel);
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        SystemViewModel rootSystemViewModel = viewModelFactory.createSystemViewModelFrom(geckoModel.getRoot());
        port = viewModelFactory.createPortViewModelIn(rootSystemViewModel);
    }

    @Test
    void run() {
        Action changeTypeAction = actionFactory.createChangeTypePortViewModelElementAction(port, "newType");
        actionManager.run(changeTypeAction);
        assertEquals("newType", port.getType());
    }

    @Test
    void getUndoAction() {
        Action changeTypeAction = actionFactory.createChangeTypePortViewModelElementAction(port, "newType");
        String beforeChangeType = port.getType();
        actionManager.run(changeTypeAction);
        actionManager.undo();
        assertEquals(beforeChangeType, port.getType());
    }
}