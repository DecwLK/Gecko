package org.gecko.actions;

import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.StateViewModel;

public class MoveEdgeViewModelElementAction extends Action {
    MoveEdgeViewModelElementAction(
            ActionFactory actionFactory,
            EdgeViewModel edgeViewModel,
            StateViewModel stateViewModel,
            boolean isSJource) {
    }

    @Override
    void run() {

    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
