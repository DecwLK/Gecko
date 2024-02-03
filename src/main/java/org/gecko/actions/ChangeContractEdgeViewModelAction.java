package org.gecko.actions;

import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.EdgeViewModel;

public class ChangeContractEdgeViewModelAction extends Action {

    private final EdgeViewModel edgeViewModel;
    private ContractViewModel oldContract;
    private final ContractViewModel newContract;

    public ChangeContractEdgeViewModelAction(EdgeViewModel edgeViewModel, ContractViewModel newContract) {
        this.edgeViewModel = edgeViewModel;
        this.newContract = newContract;
    }

    @Override
    void run() {
        oldContract = edgeViewModel.getContract();
        edgeViewModel.setContract(newContract);
        edgeViewModel.updateTarget();
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangeContractEdgeViewModelAction(edgeViewModel, oldContract);
    }
}
