package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.gecko.util.TestHelper;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CreateContractViewModelElementActionTest {

    private static StateViewModel state;
    private static ActionManager actionManager;
    private static ActionFactory actionFactory;

    @BeforeAll
    static void setUp() {
        GeckoViewModel geckoViewModel = TestHelper.createGeckoViewModel();
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        SystemViewModel rootSystemViewModel =
            viewModelFactory.createSystemViewModelFrom(geckoViewModel.getGeckoModel().getRoot());
        try {
            state = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        } catch (Exception e) {
            fail();
        }
        geckoViewModel.switchEditor(rootSystemViewModel, true);
    }

    @Test
    void run() {
        Action createContractAction = actionFactory.createCreateContractViewModelElementAction(state);
        actionManager.run(createContractAction);
        assertEquals(1, state.getContractsProperty().size());
        assertEquals(1, state.getTarget().getContracts().size());
    }

    @Test
    void getUndoAction() {
        actionManager.undo();
        assertEquals(0, state.getContractsProperty().size());
        assertEquals(0, state.getTarget().getContracts().size());
    }
}