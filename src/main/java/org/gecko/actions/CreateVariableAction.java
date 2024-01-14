package org.gecko.actions;

import org.gecko.model.Visibility;
import org.gecko.viewmodel.SystemViewModel;

public class CreateVariableAction extends Action {

    public CreateVariableAction(ActionFactory actionFactory, SystemViewModel systemViewModel, Visibility visibility) {}

    @Override
    void run() {}

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
