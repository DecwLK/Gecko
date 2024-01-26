package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.view.views.viewelement.decorator.ElementScalerBlock;

public class MoveEdgeScalerBlockViewElementAction extends Action {

    private final ElementScalerBlock elementScalerBlock;
    private final Point2D delta;

    MoveEdgeScalerBlockViewElementAction(ElementScalerBlock elementScalerBlock, Point2D delta) {
        this.elementScalerBlock = elementScalerBlock;
        this.delta = delta;
    }

    @Override
    void run() {
        elementScalerBlock.setPoint(elementScalerBlock.getPoint().add(delta));
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createMoveEdgeScalerBlockViewElementAction(elementScalerBlock, delta.multiply(-1));
    }
}
