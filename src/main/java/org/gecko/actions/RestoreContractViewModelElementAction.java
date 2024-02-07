package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
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
    boolean run() throws GeckoException {
        parent.addContract(contractViewModel);
        parent.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeleteContractViewModelAction(parent, contractViewModel);
    }
}
