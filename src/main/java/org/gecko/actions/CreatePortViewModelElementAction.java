package org.gecko.actions;

import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class CreatePortViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final SystemViewModel systemViewModel;
    private PortViewModel createdPortViewModel;

    CreatePortViewModelElementAction(GeckoViewModel geckoViewModel, SystemViewModel parentSystem) {
        this.geckoViewModel = geckoViewModel;
        this.systemViewModel = parentSystem;
    }

    @Override
    void run() {
        createdPortViewModel = geckoViewModel.getViewModelFactory().createPortViewModelIn(systemViewModel);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(createdPortViewModel);
    }
}
