package org.gecko.actions;

import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.StateViewModel;

public class DeleteContractViewModelAction extends Action {
    private final GeckoViewModel geckoViewModel;
    private final StateViewModel parent;
    private final ContractViewModel contractViewModel;

    DeleteContractViewModelAction(GeckoViewModel geckoViewModel, StateViewModel parent, ContractViewModel contractViewModel) {
        this.geckoViewModel = geckoViewModel;
        this.parent = parent;
        this.contractViewModel = contractViewModel;
    }

    @Override
    void run() {
        // TODO: remove contract without replacing state
        this.geckoViewModel.deleteViewModelElement(this.parent);
        this.parent.removeContract(this.contractViewModel);
        this.geckoViewModel.addViewModelElement(this.parent);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
