package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class RestorePortViewModelElementAction extends Action {
    private final GeckoViewModel geckoViewModel;
    private final PortViewModel portViewModel;
    private final SystemViewModel system;

    RestorePortViewModelElementAction(
        GeckoViewModel geckoViewModel, PortViewModel portViewModel, SystemViewModel system) {
        this.geckoViewModel = geckoViewModel;
        this.portViewModel = portViewModel;
        this.system = system;
    }


    @Override
    boolean run() throws GeckoException {
        system.addPort(portViewModel);
        system.updateTarget();
        geckoViewModel.addViewModelElement(portViewModel);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return new DeletePortViewModelElementAction(geckoViewModel, portViewModel, system);
    }
}
