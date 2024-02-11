package org.gecko.view.views.viewelement.decorator;

import javafx.beans.property.Property;
import javafx.collections.ListChangeListener.Change;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.view.views.viewelement.ViewElementVisitor;

/** Represents a type of {@link ElementScalerViewElementDecorator} following the decorator pattern for scaling purposes of a connection. */
public class ConnectionElementScalerViewElementDecorator extends ElementScalerViewElementDecorator {
    private static final double SCALER_SIZE = 10;
    private static final int IGNORE_Z_PRIORITY = 10000;

    private int lastEdgePointCnt;

    public ConnectionElementScalerViewElementDecorator(
        ViewElement<?> decoratorTarget) {
        super(decoratorTarget);

        // create new scaler block if edge points list change
        getEdgePoints().addListener(this::updateEdgePoints);
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
        getDecoratorTarget().accept(visitor);
    }

    @Override
    public int getZPriority() {
        return getDecoratorTarget().getZPriority() + (isSelected() ? IGNORE_Z_PRIORITY : 0);
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
        createScalerBlock(0);
        createScalerBlock(change.getList().size() - 1);
        lastEdgePointCnt = change.getList().size();
    }

    private void createScalerBlock(int change) {
        ElementScalerBlock scalerBlock = new ElementScalerBlock(change, this, SCALER_SIZE, SCALER_SIZE);
        scalerBlock.setFill(Color.RED);
        getScalers().add(scalerBlock);
        getDecoratedNode().getChildren().add(scalerBlock);
    }
}