package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.PortViewModel;

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
