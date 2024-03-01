package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

public class ContractViewModelTest {
    private StateViewModel stateViewModel;
    private EdgeViewModel edge;
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

        stateViewModel = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        StateViewModel stateViewModel2 = viewModelFactory.createStateViewModelIn(rootSystemViewModel);

        edge = viewModelFactory.createEdgeViewModelIn(rootSystemViewModel, stateViewModel, stateViewModel2);
    }

    @Test
    void createNewContract() {
        Action createContractAction = actionFactory.createCreateContractViewModelElementAction(stateViewModel);
        actionManager.run(createContractAction);
        assertEquals(1, stateViewModel.getContracts().size());
        actionManager.undo();
        assertEquals(0, stateViewModel.getContracts().size());
        actionManager.redo();
        assertEquals(1, stateViewModel.getContracts().size());
    }

    @Test
    void assignContractToEdge() {
        Action createContractAction = actionFactory.createCreateContractViewModelElementAction(stateViewModel);
        actionManager.run(createContractAction);
        assertEquals(1, stateViewModel.getContracts().size());
        ContractViewModel contractViewModel = stateViewModel.getContracts().getFirst();
        Action assignContractAction = actionFactory.createChangeContractEdgeViewModelAction(edge, contractViewModel);
        actionManager.run(assignContractAction);
        assertEquals(contractViewModel, edge.getContract());
        assertEquals(contractViewModel.getTarget(), edge.getTarget().getContract());
    }

    @Test
    void deleteContract() {
        Action createContractAction = actionFactory.createCreateContractViewModelElementAction(stateViewModel);
        actionManager.run(createContractAction);
        assertEquals(1, stateViewModel.getContracts().size());
        ContractViewModel contractViewModel = stateViewModel.getContracts().getFirst();

        Action assignContractAction = actionFactory.createChangeContractEdgeViewModelAction(edge, contractViewModel);
        actionManager.run(assignContractAction);
        assertEquals(contractViewModel, edge.getContract());

        Action deleteContractAction =
            actionFactory.createDeleteContractViewModelAction(stateViewModel, contractViewModel);
        actionManager.run(deleteContractAction);
        assertEquals(0, stateViewModel.getContracts().size());

        actionManager.run(deleteContractAction.getUndoAction(actionFactory));
        assertEquals(1, stateViewModel.getContracts().size());
        assertEquals(edge.getContract(), contractViewModel);
    }
}
