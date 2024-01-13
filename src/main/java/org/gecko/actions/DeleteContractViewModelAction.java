package org.gecko.actions;

import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;

public class DeleteContractViewModelAction extends Action {
    DeleteContractViewModelAction(
            ActionFactory actionFactory, StateViewModel parent, GeckoViewModel geckoViewModel) {}

    @Override
    void run() {}

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
