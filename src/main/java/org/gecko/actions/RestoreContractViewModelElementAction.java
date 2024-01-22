package org.gecko.actions;

import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.StateViewModel;

public class RestoreContractViewModelElementAction extends Action {

    private final StateViewModel parent;
    private final ContractViewModel contractViewModel;

    RestoreContractViewModelElementAction(StateViewModel parent, ContractViewModel contractViewModel) {
        this.parent = parent;
        this.contractViewModel = contractViewModel;
    }

    @Override
    void run() {
        parent.getContractsProperty().add(contractViewModel);
        parent.updateTarget();
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeleteContractViewModelAction(parent, contractViewModel);
    }
}
