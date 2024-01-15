package org.gecko.actions;

import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;

public class DeleteContractViewModelAction extends Action {


    DeleteContractViewModelAction(GeckoViewModel geckoViewModel, StateViewModel parent, ContractViewModel contractViewModel) {

    }

    @Override
    void run() {
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
