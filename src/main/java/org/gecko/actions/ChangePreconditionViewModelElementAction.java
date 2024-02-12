package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.ContractViewModel;

/**
 * A concrete representation of an {@link Action} that changes the precondition of a {@link ContractViewModel}, which it
 * holds a reference to. Additionally, holds the old and new {@link String precondition}s of the contract for undo/redo
 * purposes.
 */
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
        if (newPrecondition.isEmpty()) {
            return false;
        }
        contractViewModel.setPrecondition(newPrecondition);
        contractViewModel.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangePreconditionViewModelElementAction(contractViewModel, oldPrecondition);
    }
}
