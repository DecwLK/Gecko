package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.view.views.viewelement.decorator.ConnectionElementScalerViewElementDecorator;
import org.gecko.view.views.viewelement.decorator.ElementScalerBlock;

public class DeleteEdgeScalerBlockViewElementAction extends Action {

    private final ConnectionElementScalerViewElementDecorator decorator;
    private final ElementScalerBlock scalerBlock;

    private Point2D lastPosition;

    DeleteEdgeScalerBlockViewElementAction(
        ConnectionElementScalerViewElementDecorator decorator, ElementScalerBlock scalerBlock) {
        this.decorator = decorator;
        this.scalerBlock = scalerBlock;
    }

    @Override
    void run() {
        lastPosition = scalerBlock.getPoint();
        decorator.deletePoint(scalerBlock);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createCreateEdgeScalerBlockViewElementAction(decorator, lastPosition);
    }
}
