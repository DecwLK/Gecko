package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.ViewModelFactory;

public class CreateSystemViewModelElementAction extends Action {

    CreateSystemViewModelElementAction(
            ActionFactory actionFactory,
            ViewModelFactory viewModelFactory,
            GeckoViewModel geckoViewModel,
            Point2D position) {}

    @Override
    void run() {}

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
