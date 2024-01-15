package org.gecko.actions;

import javafx.geometry.Point2D;

import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class CreateRegionViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final Point2D position;
    private RegionViewModel createdRegionViewModel;

    CreateRegionViewModelElementAction(GeckoViewModel geckoViewModel, Point2D position) {
        this.geckoViewModel = geckoViewModel;
        this.position = position;
    }

    @Override
    void run() {
        SystemViewModel currentParentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem();
        createdRegionViewModel = geckoViewModel.getViewModelFactory().createRegionViewModelIn(currentParentSystem);
        createdRegionViewModel.setPosition(position);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(createdRegionViewModel);
    }
}
