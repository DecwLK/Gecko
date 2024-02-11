package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.SystemViewModel;

/** A concrete representation of an {@link Action} that removes a {@link PortViewModel} from the given parent-{@link SystemViewModel}. */
public class DeletePortViewModelElementAction extends AbstractPositionableViewModelElementAction {
    private final GeckoViewModel geckoViewModel;
    private final PortViewModel portViewModel;
    private final SystemViewModel system;

    DeletePortViewModelElementAction(
        GeckoViewModel geckoViewModel, PortViewModel portViewModel, SystemViewModel system) {
        this.geckoViewModel = geckoViewModel;
        this.portViewModel = portViewModel;
        this.system = system;
    }

    @Override
    boolean run() throws GeckoException {
        system.removePort(portViewModel);
        system.updateTarget();
        geckoViewModel.deleteViewModelElement(portViewModel);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return new RestorePortViewModelElementAction(geckoViewModel, portViewModel, system);
    }

    @Override
    PositionableViewModelElement<?> getTarget() {
        return portViewModel;
    }
}
