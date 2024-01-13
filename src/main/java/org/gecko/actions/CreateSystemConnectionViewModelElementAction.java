package org.gecko.actions;

import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.ViewModelFactory;

public class CreateSystemConnectionViewModelElementAction extends Action {

    CreateSystemConnectionViewModelElementAction(
            ActionFactory actionFactory,
            ViewModelFactory viewModelFactory,
            GeckoViewModel geckoViewModel,
            PortViewModel source,
            PortViewModel destination) {}

    @Override
    void run() {}

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
