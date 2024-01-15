package org.gecko.actions;

import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;

public class MoveSystemConnectionViewModelElementAction extends Action {

    private final SystemConnectionViewModel systemConnectionViewModel;
    private final PortViewModel portViewModel;
    private final boolean isSource;

    MoveSystemConnectionViewModelElementAction(SystemConnectionViewModel systemConnectionViewModel, PortViewModel portViewModel, boolean isSource) {
        this.systemConnectionViewModel = systemConnectionViewModel;
        this.portViewModel = portViewModel;
        this.isSource = isSource;
    }

    @Override
    void run() {
        if (isSource) {
            systemConnectionViewModel.setSource(portViewModel);
        } else {
            systemConnectionViewModel.setDestination(portViewModel);
        }
        systemConnectionViewModel.updateTarget();
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createMoveSystemConnectionViewModelElementAction(systemConnectionViewModel,
            isSource ? systemConnectionViewModel.getSource() : systemConnectionViewModel.getDestination(), isSource);
    }
}
