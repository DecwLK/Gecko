package org.gecko.actions;

import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class MoveSystemConnectionViewModelElementAction extends Action {

    MoveSystemConnectionViewModelElementAction(
            ActionFactory actionFactory,
            SystemConnectionViewModel systemConnectionViewModel,
            SystemViewModel systemViewModel,
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
