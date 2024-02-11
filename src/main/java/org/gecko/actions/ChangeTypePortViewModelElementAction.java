package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.PortViewModel;

/** A concrete representation of an {@link Action} that changes the type of a {@link PortViewModel}, which it holds a reference to. Additionally holds the old and new {@link String type}s of the contract for undo/redo purposes. */
public class ChangeTypePortViewModelElementAction extends Action {

    private final PortViewModel portViewModel;
    private final String newType;
    private final String oldType;

    ChangeTypePortViewModelElementAction(PortViewModel portViewModel, String newType) {
        this.portViewModel = portViewModel;
        this.newType = newType;
        this.oldType = portViewModel.getType();
    }

    @Override
    boolean run() throws GeckoException {
        if (newType.isEmpty()) {
            return false;
        }
        portViewModel.setType(newType);
        portViewModel.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangeTypePortViewModelElementAction(portViewModel, oldType);
    }
}
