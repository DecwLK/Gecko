package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.view.views.viewelement.decorator.ConnectionElementScalerViewElementDecorator;
import org.gecko.view.views.viewelement.decorator.ElementScalerBlock;
import org.gecko.viewmodel.EditorViewModel;

public class CreateEdgeScalerBlockViewElementAction extends Action {

    private final EditorViewModel editorViewModel;
    private final ConnectionElementScalerViewElementDecorator decorator;
    private final Point2D position;

    private ElementScalerBlock newScalerBlock;

    CreateEdgeScalerBlockViewElementAction(
        EditorViewModel editorViewModel, ConnectionElementScalerViewElementDecorator decorator, Point2D position) {
        this.editorViewModel = editorViewModel;
        this.decorator = decorator;
        this.position = position;
    }

    @Override
    void run() {
        newScalerBlock = decorator.createNewPoint(editorViewModel.transformScreenToWorldCoordinates(position));
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDestroyEdgeScalerBlockViewElementAction(decorator, newScalerBlock);
    }
}
