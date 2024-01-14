package org.gecko.actions;

import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.StateViewModel;

public class MoveEdgeViewModelElementAction extends Action {
    MoveEdgeViewModelElementAction(
            EdgeViewModel edgeViewModel,
            StateViewModel stateViewModel,
            boolean isSource) {
    }

    @Override
    void run() {

    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
