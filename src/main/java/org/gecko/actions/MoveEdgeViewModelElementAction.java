package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.StateViewModel;

public class MoveEdgeViewModelElementAction extends Action {

    private final EdgeViewModel edgeViewModel;
    private final StateViewModel stateViewModel;
    private final boolean isSource;

    MoveEdgeViewModelElementAction(EdgeViewModel edgeViewModel, StateViewModel stateViewModel, boolean isSource) {
        this.edgeViewModel = edgeViewModel;
        this.stateViewModel = stateViewModel;
        this.isSource = isSource;
    }

    @Override
    boolean run() throws GeckoException {
        if (isSource) {
            edgeViewModel.setSource(stateViewModel);
        } else {
            edgeViewModel.setDestination(stateViewModel);
        }
        edgeViewModel.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createMoveEdgeViewModelElementAction(edgeViewModel,
            isSource ? edgeViewModel.getSource() : edgeViewModel.getDestination(), isSource);
    }
}
