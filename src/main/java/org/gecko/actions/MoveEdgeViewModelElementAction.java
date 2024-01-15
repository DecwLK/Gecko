package org.gecko.actions;

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
    void run() {
        if (isSource) {
            edgeViewModel.setSource(stateViewModel);
        } else {
            edgeViewModel.setDestination(stateViewModel);
        }
        edgeViewModel.updateTarget();
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createMoveEdgeViewModelElementAction(edgeViewModel,
            isSource ? edgeViewModel.getSource() : edgeViewModel.getDestination(), isSource);
    }
}
