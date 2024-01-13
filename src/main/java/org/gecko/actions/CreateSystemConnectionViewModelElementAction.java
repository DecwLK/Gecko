package org.gecko.actions;

import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.ViewModelFactory;

public class CreateSystemConnectionViewModelElementAction extends Action {

    CreateSystemConnectionViewModelElementAction(
            ActionFactory actionFactory,
            GeckoViewModel geckoViewModel,
            ViewModelFactory viewModelFactory,
            PortViewModel source,
            PortViewModel destination) {}

    @Override
    void run() {}

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
