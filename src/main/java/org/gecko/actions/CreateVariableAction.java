package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;

public class CreateVariableAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final Point2D position;
    private PortViewModel createdPortViewModel;

    CreateVariableAction(GeckoViewModel geckoViewModel, Point2D position) {
        this.geckoViewModel = geckoViewModel;
        this.position = position;
    }

    @Override
    void run() {
        createdPortViewModel = geckoViewModel.getViewModelFactory().createPortViewModelIn(geckoViewModel.getCurrentEditor().getCurrentSystem());
        createdPortViewModel.setPosition(position);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(createdPortViewModel);
    }
}
