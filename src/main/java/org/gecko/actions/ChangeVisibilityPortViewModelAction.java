package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.model.Visibility;
import org.gecko.viewmodel.PortViewModel;

public class ChangeVisibilityPortViewModelAction extends Action {

    private final Visibility visibility;
    private final Visibility oldVisibility;
    private final PortViewModel portViewModel;

    public ChangeVisibilityPortViewModelAction(PortViewModel portViewModel, Visibility visibility) {
        this.portViewModel = portViewModel;
        this.visibility = visibility;
        this.oldVisibility = portViewModel.getVisibility();
    }

    @Override
    boolean run() throws GeckoException {
        portViewModel.setVisibility(visibility);
        portViewModel.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangeVisibilityPortViewModelAction(portViewModel, oldVisibility);
    }
}
