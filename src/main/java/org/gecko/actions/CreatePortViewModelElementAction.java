package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.ViewModelFactory;

public class CreatePortViewModelElementAction extends Action {
    CreatePortViewModelElementAction(
            ActionFactory actionFactory,
            GeckoViewModel geckoViewModel,
            ViewModelFactory viewModelFactory,
            Point2D position) {}

    CreatePortViewModelElementAction(
            ActionFactory actionFactory,
            GeckoViewModel geckoViewModel,
            ViewModelFactory viewModelFactory
            ) {}

    @Override
    void run() {}

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
