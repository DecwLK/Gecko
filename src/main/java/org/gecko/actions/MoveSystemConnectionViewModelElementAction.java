package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.model.System;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class MoveSystemConnectionViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final SystemConnectionViewModel systemConnectionViewModel;
    private final PortViewModel portViewModel;
    private final boolean isSource;

    MoveSystemConnectionViewModelElementAction(
        GeckoViewModel geckoViewModel, SystemConnectionViewModel systemConnectionViewModel, PortViewModel portViewModel,
        boolean isSource) {
        this.geckoViewModel = geckoViewModel;
        this.systemConnectionViewModel = systemConnectionViewModel;
        this.portViewModel = portViewModel;
        this.isSource = isSource;
    }

    @Override
    boolean run() throws GeckoException {
        SystemViewModel currentSystemViewModel = geckoViewModel.getCurrentEditor().getCurrentSystem();
        System containingSystem =
            currentSystemViewModel.getTarget().getChildSystemWithVariable(portViewModel.getTarget());
        SystemViewModel containingSystemViewModel =
            (SystemViewModel) geckoViewModel.getViewModelElement(containingSystem);
        if (isSource) {
            if (containingSystemViewModel.getPorts().contains(systemConnectionViewModel.getDestination())) {
                return false;
            }
            systemConnectionViewModel.setSource(portViewModel);
        } else {
            if (containingSystemViewModel.getPorts().contains(systemConnectionViewModel.getSource())) {
                return false;
            }
            if (portViewModel.getTarget().isHasIncomingConnection()) {
                return false;
            }
            systemConnectionViewModel.setDestination(portViewModel);
        }
        systemConnectionViewModel.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createMoveSystemConnectionViewModelElementAction(systemConnectionViewModel,
            isSource ? systemConnectionViewModel.getSource() : systemConnectionViewModel.getDestination(), isSource);
    }
}
