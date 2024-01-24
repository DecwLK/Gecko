package org.gecko.actions;

import org.gecko.model.Visibility;
import org.gecko.viewmodel.PortViewModel;

public class ChangeVisibilityPortViewModelAction extends Action {

    private final Visibility visibility;
    private final PortViewModel portViewModel;

    public ChangeVisibilityPortViewModelAction(PortViewModel portViewModel, Visibility visibility) {
        this.portViewModel = portViewModel;
        this.visibility = visibility;
    }

    @Override
    void run() {
        portViewModel.setVisibility(visibility);
        portViewModel.updateTarget();
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangeVisibilityPortViewModelAction(portViewModel, portViewModel.getVisibility());
    }
}
