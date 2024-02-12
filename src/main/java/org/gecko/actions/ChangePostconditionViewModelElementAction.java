package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.ContractViewModel;

/**
 * A concrete representation of an {@link Action} that changes the postcondition of a {@link ContractViewModel}, which
 * it holds a reference to. Additionally, holds the old and new {@link String postcondition}s of the contract for
 * undo/redo purposes.
 */
public class ChangePostconditionViewModelElementAction extends Action {

    private final ContractViewModel contractViewModel;
    private final String newPostcondition;
    private final String oldPostcondition;

    ChangePostconditionViewModelElementAction(ContractViewModel contractViewModel, String newPostcondition) {
        this.contractViewModel = contractViewModel;
        this.newPostcondition = newPostcondition;
        this.oldPostcondition = contractViewModel.getPostcondition();
    }

    @Override
    boolean run() throws GeckoException {
        if (newPostcondition.isEmpty()) {
            return false;
        }
        contractViewModel.setPostcondition(newPostcondition);
        contractViewModel.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangePostconditionViewModelElementAction(contractViewModel, oldPostcondition);
    }
}
