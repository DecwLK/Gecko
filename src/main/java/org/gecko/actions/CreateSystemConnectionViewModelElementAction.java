package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

/**
 * A concrete representation of an {@link Action} that creates an {@link SystemConnectionViewModel} in the
 * current-{@link SystemViewModel} with given source- and destination-{@link PortViewModel}s through the
 * {@link org.gecko.viewmodel.ViewModelFactory ViewModelFactory} of the {@link GeckoViewModel}.
 */
public class CreateSystemConnectionViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final PortViewModel source;
    private final PortViewModel destination;
    private SystemConnectionViewModel createdSystemConnectionViewModel;

    CreateSystemConnectionViewModelElementAction(
        GeckoViewModel geckoViewModel, PortViewModel source, PortViewModel destination) {
        this.geckoViewModel = geckoViewModel;
        this.source = source;
        this.destination = destination;
    }

    @Override
    boolean run() throws GeckoException {
        SystemViewModel currentParentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem();
        if (!SystemConnectionViewModel.isConnectingAllowed(source, destination,
            geckoViewModel.getSystemViewModelWithPort(source), geckoViewModel.getSystemViewModelWithPort(destination),
            currentParentSystem, null)) {
            return false;
        }

        createdSystemConnectionViewModel = geckoViewModel.getViewModelFactory()
            .createSystemConnectionViewModelIn(currentParentSystem, source, destination);

        ActionManager actionManager = geckoViewModel.getActionManager();
        actionManager.run(actionManager.getActionFactory().createSelectAction(createdSystemConnectionViewModel, true));
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(createdSystemConnectionViewModel);
    }
}
