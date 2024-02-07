package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.model.System;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.SystemConnectionViewModel;

public class DeleteSystemConnectionViewModelElementAction extends AbstractPositionableViewModelElementAction {
    private final GeckoViewModel geckoViewModel;
    private final SystemConnectionViewModel systemConnectionViewModel;
    private final System system;

    DeleteSystemConnectionViewModelElementAction(
        GeckoViewModel geckoViewModel, SystemConnectionViewModel systemConnectionViewModel, System system) {
        this.geckoViewModel = geckoViewModel;
        this.systemConnectionViewModel = systemConnectionViewModel;
        this.system = system;
    }

    @Override
    boolean run() throws GeckoException {
        system.removeConnection(systemConnectionViewModel.getTarget());
        geckoViewModel.deleteViewModelElement(systemConnectionViewModel);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return new RestoreSystemConnectionViewModelElementAction(geckoViewModel, systemConnectionViewModel, system);
    }

    @Override
    PositionableViewModelElement<?> getTarget() {
        return systemConnectionViewModel;
    }
}
