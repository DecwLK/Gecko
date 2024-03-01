package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.gecko.exceptions.ModelException;
import org.gecko.util.TestHelper;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChangeContractEdgeViewModelActionTest {
    private EdgeViewModel edge;
    private ActionManager actionManager;
    private ActionFactory actionFactory;
    private ContractViewModel contractViewModel;

    @BeforeEach
    void setUp() throws ModelException {
        GeckoViewModel geckoViewModel = TestHelper.createGeckoViewModel();
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        SystemViewModel rootSystemViewModel =
            viewModelFactory.createSystemViewModelFrom(geckoViewModel.getGeckoModel().getRoot());

        StateViewModel stateViewModel1 = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        StateViewModel stateViewModel2 = viewModelFactory.createStateViewModelIn(rootSystemViewModel);

        edge = viewModelFactory.createEdgeViewModelIn(rootSystemViewModel, stateViewModel1, stateViewModel2);
        contractViewModel = viewModelFactory.createContractViewModelIn(stateViewModel1);
    }

    @Test
    void run() {
        Action changeContractAction = actionFactory.createChangeContractEdgeViewModelAction(edge, contractViewModel);
        actionManager.run(changeContractAction);
        assertEquals(contractViewModel, edge.getContract());
        assertEquals(contractViewModel.getTarget(), edge.getTarget().getContract());
    }

    @Test
    void getUndoAction() {
        Action changeContractAction = actionFactory.createChangeContractEdgeViewModelAction(edge, contractViewModel);
        ContractViewModel beforeChangeContract = edge.getContract();
        actionManager.run(changeContractAction);
        actionManager.undo();
        assertNull(edge.getContract());
    }
}
