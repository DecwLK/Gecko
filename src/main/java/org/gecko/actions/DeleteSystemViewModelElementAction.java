package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.model.System;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.SystemViewModel;

public class DeleteSystemViewModelElementAction extends AbstractPositionableViewModelElementAction {
    private final GeckoViewModel geckoViewModel;
    private final SystemViewModel systemViewModel;
    private final System system;

    DeleteSystemViewModelElementAction(GeckoViewModel geckoViewModel, SystemViewModel systemViewModel, System system) {
        this.geckoViewModel = geckoViewModel;
        this.systemViewModel = systemViewModel;
        this.system = system;
    }

    @Override
    boolean run() throws GeckoException {
        system.removeChild(systemViewModel.getTarget());
        geckoViewModel.deleteViewModelElement(systemViewModel);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return new RestoreSystemViewModelElementAction(geckoViewModel, systemViewModel, system);
    }

    @Override
    PositionableViewModelElement<?> getTarget() {
        return systemViewModel;
    }
}
