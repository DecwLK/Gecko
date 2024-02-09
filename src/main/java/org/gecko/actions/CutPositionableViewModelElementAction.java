package org.gecko.actions;

import org.gecko.exceptions.GeckoException;

public class CutPositionableViewModelElementAction extends Action {
    CopyPositionableViewModelElementAction copyAction;
    ActionGroup deleteActions;

    CutPositionableViewModelElementAction(
        CopyPositionableViewModelElementAction copyAction, ActionGroup deleteActions) {
        this.copyAction = copyAction;
        this.deleteActions = deleteActions;
    }

    @Override
    boolean run() throws GeckoException {
        copyAction.run();
        boolean delete = deleteActions.run();
        return delete;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
