package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
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
    boolean run() throws GeckoException {
        contractViewModel.setPrecondition(newPrecondition);
        contractViewModel.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangePreconditionViewModelElementAction(contractViewModel, oldPrecondition);
    }
}
