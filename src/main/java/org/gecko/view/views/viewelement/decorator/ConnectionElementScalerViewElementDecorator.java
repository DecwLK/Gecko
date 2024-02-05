package org.gecko.view.views.viewelement.decorator;

import javafx.beans.property.Property;
import javafx.collections.ListChangeListener.Change;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.view.views.viewelement.ViewElementVisitor;

public class ConnectionElementScalerViewElementDecorator extends ElementScalerViewElementDecorator {
    private static final double SCALER_SIZE = 10;

    private int lastEdgePointCnt;

    public ConnectionElementScalerViewElementDecorator(
        ViewElement<?> decoratorTarget) {
        super(decoratorTarget);

        getEdgePoints().addListener(this::updateEdgePoints);
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
        getDecoratorTarget().accept(visitor);
    }

    private void updateEdgePoints(Change<? extends Property<Point2D>> change) {
        if (change.getList().size() == lastEdgePointCnt) {

            for (ElementScalerBlock scaler : getScalers()) {
                scaler.refreshListeners();
            }

            return;
        }

        getDecoratedNode().getChildren().removeAll(getScalers());
        getScalers().clear();

        for (int i = 0; i < change.getList().size(); i++) {
            ElementScalerBlock scalerBlock = new ElementScalerBlock(i, this, SCALER_SIZE, SCALER_SIZE);
            scalerBlock.setFill(Color.RED);

            getScalers().add(scalerBlock);
            getDecoratedNode().getChildren().add(scalerBlock);
        }

        lastEdgePointCnt = change.getList().size();
    }
}