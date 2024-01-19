package org.gecko.actions;

import org.gecko.model.DeleteElementVisitor;
import org.gecko.model.ElementVisitor;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.GeckoViewModel;
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
        parent.getContractsProperty().remove(contractViewModel);
        ElementVisitor deleteElementVisitor = new DeleteElementVisitor(geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget());
        contractViewModel.getTarget().accept(deleteElementVisitor);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createRestoreContractViewModelElementAction(parent, contractViewModel);
    }
}
