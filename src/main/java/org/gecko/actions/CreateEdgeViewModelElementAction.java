package org.gecko.actions;

import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;

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
    void run() {
        SystemViewModel currentParentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem();
        createdEdgeViewModel = geckoViewModel.getViewModelFactory().createEdgeViewModelIn(currentParentSystem, source, destination);
        createdEdgeViewModel.updateTarget();
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(createdEdgeViewModel);
    }
}
