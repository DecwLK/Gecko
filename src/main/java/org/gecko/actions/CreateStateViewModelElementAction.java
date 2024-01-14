package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class CreateStateViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final Point2D position;
    private StateViewModel createdStateViewModel;

    CreateStateViewModelElementAction(
            GeckoViewModel geckoViewModel,
            Point2D position) {
        this.geckoViewModel = geckoViewModel;
        this.position = position;
    }

    @Override
    void run() {
        SystemViewModel currentParentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem();
        createdStateViewModel = geckoViewModel.getViewModelFactory().createStateViewModelIn(currentParentSystem);
        //TODO use property conventions
        createdStateViewModel.setPosition(position);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(createdStateViewModel);
    }
}
