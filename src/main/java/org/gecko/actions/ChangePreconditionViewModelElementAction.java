package org.gecko.actions;

import org.gecko.viewmodel.ContractViewModel;

public class ChangePreconditionViewModelElementAction extends Action {

    private final ContractViewModel contractViewModel;
    private final String newPrecondition;
    private final String oldPrecondition;

    ChangePreconditionViewModelElementAction(ContractViewModel contractViewModel, String newPrecondition) {
        this.contractViewModel = contractViewModel;
        this.newPrecondition = newPrecondition;
        this.oldPrecondition = contractViewModel.getPrecondition();
    }

    @Override
    void run() {
        contractViewModel.setPrecondition(newPrecondition);
        contractViewModel.updateTarget();
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangePreconditionViewModelElementAction(contractViewModel, oldPrecondition);
    }
}
