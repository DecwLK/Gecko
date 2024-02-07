package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;

public class DeleteContractViewModelAction extends Action {
    private final GeckoViewModel geckoViewModel;
    private final StateViewModel parent;
    private final ContractViewModel contractViewModel;

    DeleteContractViewModelAction(
        GeckoViewModel geckoViewModel, StateViewModel parent, ContractViewModel contractViewModel) {
        this.geckoViewModel = geckoViewModel;
        this.parent = parent;
        this.contractViewModel = contractViewModel;
    }

    @Override
    boolean run() throws GeckoException {
        parent.removeContract(contractViewModel);
        parent.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createRestoreContractViewModelElementAction(parent, contractViewModel);
    }
}
