package org.gecko.actions;

import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.EdgeViewModel;

public class ChangeContractEdgeViewModelAction extends Action {

    private final EdgeViewModel viewModel;
    private ContractViewModel oldContract;
    private final ContractViewModel newContract;

    public ChangeContractEdgeViewModelAction(EdgeViewModel viewModel, ContractViewModel newContract) {
        this.viewModel = viewModel;
        this.newContract = newContract;
    }

    @Override
    void run() {
        oldContract = viewModel.getContract();
        viewModel.setContract(newContract);
        viewModel.updateTarget();
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangeContractEdgeViewModelAction(viewModel, oldContract);
    }
}
