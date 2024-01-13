package org.gecko.actions;

import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.ViewModelFactory;

public class CreateEdgeViewModelElementAction extends Action {

    CreateEdgeViewModelElementAction(
            ActionFactory actionFactory,
            GeckoViewModel geckoViewModel,
            ViewModelFactory viewModelFactory,
            StateViewModel source,
            StateViewModel destination) {
    }

    @Override
    void run() {

    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
