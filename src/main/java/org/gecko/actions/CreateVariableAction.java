package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;

public class CreateVariableAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final EditorViewModel editorViewModel;
    private final Point2D position;
    private PortViewModel createdPortViewModel;

    CreateVariableAction(GeckoViewModel geckoViewModel, EditorViewModel editorViewModel, Point2D position) {
        this.geckoViewModel = geckoViewModel;
        this.editorViewModel = editorViewModel;
        this.position = position;
    }

    @Override
    void run() {
        createdPortViewModel = geckoViewModel.getViewModelFactory()
            .createPortViewModelIn(geckoViewModel.getCurrentEditor().getCurrentSystem());
        createdPortViewModel.setCenter(editorViewModel.transformScreenToWorldCoordinates(position));
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(createdPortViewModel);
    }
}
