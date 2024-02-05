package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.exceptions.GeckoException;
import org.gecko.view.views.viewelement.decorator.ConnectionElementScalerViewElementDecorator;
import org.gecko.view.views.viewelement.decorator.ElementScalerBlock;

public class CreateConnectionScalerBlockViewElementAction extends Action {

    private final ConnectionElementScalerViewElementDecorator decorator;
    private final Point2D position;

    private ElementScalerBlock newScalerBlock;

    CreateConnectionScalerBlockViewElementAction(
        ConnectionElementScalerViewElementDecorator decorator, Point2D position) {
        this.decorator = decorator;
        this.position = position;
    }

    @Override
    boolean run() throws GeckoException {
        newScalerBlock = decorator.createNewPoint(position);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDestroyConnectionScalerBlockViewElementAction(decorator, newScalerBlock);
    }
}
