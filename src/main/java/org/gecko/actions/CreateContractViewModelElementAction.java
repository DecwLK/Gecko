package org.gecko.actions;

import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;

public class CreateContractViewModelElementAction extends Action {

    CreateContractViewModelElementAction(
            ActionFactory actionFactory,
            StateViewModel stateViewModel,
            GeckoViewModel geckoViewModel) {}

    @Override
    void run() {}

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
