package org.gecko.view.views.viewelement.decorator;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener.Change;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.view.views.viewelement.ViewElementVisitor;

public class ConnectionElementScalerViewElementDecorator extends ElementScalerViewElementDecorator {
    private static final double SCALER_SIZE = 10;

    public ConnectionElementScalerViewElementDecorator(
        ViewElement<?> decoratorTarget) {
        super(decoratorTarget);

        getEdgePoints().addListener(this::updateEdgePoints);
    }

    public ElementScalerBlock createNewPoint(Point2D point) {
        // Find the index of the edge point that is closest to the input point.
        int minIndex = findMinimumIndex(point);

        if (minIndex == 0) {
            minIndex = 1;
        }

        getEdgePoints().add(minIndex, new SimpleObjectProperty<>(point));
        return getScalers().get(minIndex);
    }

    private int findMinimumIndex(Point2D point) {
        int minIndex = 0;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < getEdgePoints().size() - 1; i++) {
            Point2D p1 = getEdgePoints().get(i).getValue();
            Point2D p2 = getEdgePoints().get(i + 1).getValue();
            double distance = distanceToSegment(point, p1, p2);

            if (distance < minDistance) {
                minDistance = distance;
                minIndex = i;
            }
        }

        return minIndex + 1;
    }

    public void deletePoint(ElementScalerBlock scalerBlock) {
        int index = getScalers().indexOf(scalerBlock);
        getEdgePoints().remove(index);
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
        getDecoratorTarget().accept(visitor);
    }

    private void updateEdgePoints(Change<? extends Property<Point2D>> change) {
        getDecoratedNode().getChildren().removeAll(getScalers());
        getScalers().clear();

        for (int i = 0; i < change.getList().size(); i++) {
            ElementScalerBlock scalerBlock = new ElementScalerBlock(i, this, SCALER_SIZE, SCALER_SIZE);
            scalerBlock.setFill(Color.RED);

            getScalers().add(scalerBlock);
            getDecoratedNode().getChildren().add(scalerBlock);
        }
    }

    // Returns the distance between a point and a line segment (p1 and p2).
    private static double distanceToSegment(Point2D point, Point2D p1, Point2D p2) {
        Point2D p1p2 = p2.subtract(p1);
        Point2D p2p = point.subtract(p2);
        Point2D p1p = point.subtract(p1);

        // Compute the dot product of p1p2 and p2p.
        double dotP2 = p1p2.dotProduct(p2p);
        double dotP1 = p1p2.dotProduct(p1p);

        double result;

        if (dotP2 > 0) {
            result = point.distance(p2);
        } else if (dotP1 < 0) {
            result = point.distance(p1);
        } else {
            // Find the perpendicular distance from the point to the line segment.
            double mod = Math.sqrt(Math.pow(p1p2.getX(), 2) + Math.pow(p1p2.getY(), 2));
            result = Math.abs((p1p2.getX() * p1p.getY()) - (p1p.getX() * p1p2.getY())) / mod;
        }

        return result;
    }
}