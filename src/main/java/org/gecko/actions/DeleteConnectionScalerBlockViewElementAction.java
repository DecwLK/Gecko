package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.exceptions.GeckoException;
import org.gecko.view.views.viewelement.decorator.ConnectionElementScalerViewElementDecorator;
import org.gecko.view.views.viewelement.decorator.ElementScalerBlock;

public class DeleteConnectionScalerBlockViewElementAction extends Action {

    private final ConnectionElementScalerViewElementDecorator decorator;
    private final ElementScalerBlock scalerBlock;

    private Point2D lastPosition;

    DeleteConnectionScalerBlockViewElementAction(
        ConnectionElementScalerViewElementDecorator decorator, ElementScalerBlock scalerBlock) {
        this.decorator = decorator;
        this.scalerBlock = scalerBlock;
    }

    @Override
    boolean run() throws GeckoException {
        lastPosition = scalerBlock.getPoint();
        decorator.deletePoint(scalerBlock);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createCreateConnectionScalerBlockViewElementAction(decorator, lastPosition);
    }
}
