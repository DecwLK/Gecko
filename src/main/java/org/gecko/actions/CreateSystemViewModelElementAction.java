package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class CreateSystemViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final Point2D position;
    private SystemViewModel createdSystemViewModel;

    CreateSystemViewModelElementAction(
            GeckoViewModel geckoViewModel,
            Point2D position) {
        this.geckoViewModel = geckoViewModel;
        this.position = position;
    }

    @Override
    void run() {
        SystemViewModel currentParentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem();
        createdSystemViewModel = geckoViewModel.getViewModelFactory().createSystemViewModelIn(currentParentSystem);
        createdSystemViewModel.setPosition(position);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(createdSystemViewModel);
    }
}
