package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.EdgeViewModel;

/** A concrete representation of an {@link Action} that changes the contract of an {@link EdgeViewModel}, which it holds a reference to. Additionally holds the old and new {@link ContractViewModel}s of the edge for undo/redo purposes. */
public class ChangeContractEdgeViewModelAction extends Action {

    private final EdgeViewModel edgeViewModel;
    private ContractViewModel oldContract;
    private final ContractViewModel newContract;

    public ChangeContractEdgeViewModelAction(EdgeViewModel edgeViewModel, ContractViewModel newContract) {
        this.edgeViewModel = edgeViewModel;
        this.newContract = newContract;
    }

    @Override
    boolean run() throws GeckoException {
        oldContract = edgeViewModel.getContract();
        edgeViewModel.setContract(newContract);
        edgeViewModel.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangeContractEdgeViewModelAction(edgeViewModel, oldContract);
    }
}
