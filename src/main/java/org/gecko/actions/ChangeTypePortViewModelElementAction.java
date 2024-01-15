package org.gecko.actions;

import org.gecko.viewmodel.PortViewModel;

public class ChangeTypePortViewModelElementAction extends Action {

    private final PortViewModel portViewModel;
    private final String newType;

    private final String oldType;

    public ChangeTypePortViewModelElementAction(PortViewModel portViewModel, String newType) {
        this.portViewModel = portViewModel;
        this.newType = newType;
        this.oldType = portViewModel.getType();
    }

    @Override
    void run() {
        portViewModel.setType(newType);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangeTypePortViewModelElementAction(portViewModel, oldType);
    }
}
