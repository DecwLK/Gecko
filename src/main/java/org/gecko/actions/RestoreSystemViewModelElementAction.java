package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.model.System;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class RestoreSystemViewModelElementAction extends Action {
    private final GeckoViewModel geckoViewModel;
    private final SystemViewModel systemViewModel;
    private final System system;

    RestoreSystemViewModelElementAction(
        GeckoViewModel geckoViewModel, SystemViewModel systemViewModel, System system) {
        this.geckoViewModel = geckoViewModel;
        this.systemViewModel = systemViewModel;
        this.system = system;
    }


    @Override
    boolean run() throws GeckoException {
        system.addChild(systemViewModel.getTarget());
        geckoViewModel.addViewModelElement(systemViewModel);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return new DeleteSystemViewModelElementAction(geckoViewModel, systemViewModel, system);
    }
}
