package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;

/**
 * A concrete representation of an {@link Action} that creates an {@link EdgeViewModel} in the
 * current-{@link SystemViewModel} with given source- and destination-{@link StateViewModel}s through the
 * {@link org.gecko.viewmodel.ViewModelFactory ViewModelFactory} of the {@link GeckoViewModel}.
 */
public class CreateEdgeViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final StateViewModel source;
    private final StateViewModel destination;
    private EdgeViewModel createdEdgeViewModel;

    CreateEdgeViewModelElementAction(GeckoViewModel geckoViewModel, StateViewModel source, StateViewModel destination) {
        this.geckoViewModel = geckoViewModel;
        this.source = source;
        this.destination = destination;
    }

    @Override
    boolean run() throws GeckoException {
        SystemViewModel currentParentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem();
        createdEdgeViewModel =
            geckoViewModel.getViewModelFactory().createEdgeViewModelIn(currentParentSystem, source, destination);
        ActionManager actionManager = geckoViewModel.getActionManager();
        actionManager.run(actionManager.getActionFactory().createSelectAction(createdEdgeViewModel, true));
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(createdEdgeViewModel);
    }
}
