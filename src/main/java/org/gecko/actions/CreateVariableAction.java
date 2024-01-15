package org.gecko.actions;

import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class CreateVariableAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final SystemViewModel parentSystem;
    private PortViewModel createdPortViewModel;

    CreateVariableAction(GeckoViewModel geckoViewModel, SystemViewModel parentSystem) {
        this.geckoViewModel = geckoViewModel;
        this.parentSystem = parentSystem;
    }

    @Override
    void run() {
        createdPortViewModel = geckoViewModel.getViewModelFactory().createPortViewModelIn(parentSystem);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(createdPortViewModel);
    }
}
