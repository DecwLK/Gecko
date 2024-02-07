package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.model.System;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;

public class RestoreSystemConnectionViewModelElementAction extends Action {
    private final GeckoViewModel geckoViewModel;
    private final SystemConnectionViewModel systemConnectionViewModel;
    private final System system;

    RestoreSystemConnectionViewModelElementAction(
        GeckoViewModel geckoViewModel, SystemConnectionViewModel systemConnectionViewModel, System system) {
        this.geckoViewModel = geckoViewModel;
        this.systemConnectionViewModel = systemConnectionViewModel;
        this.system = system;
    }


    @Override
    boolean run() throws GeckoException {
        system.addConnection(systemConnectionViewModel.getTarget());
        geckoViewModel.addViewModelElement(systemConnectionViewModel);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return new DeleteSystemConnectionViewModelElementAction(geckoViewModel, systemConnectionViewModel, system);
    }
}
