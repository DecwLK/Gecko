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
        return scalers.get(minIndex);
    }

    public int findMinimumIndex(Point2D point) {
        int minIndex = -1;
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

        return minIndex;
    }

    public static double distanceToSegment(Point2D point, Point2D p1, Point2D p2) {
        double xDelta = p2.getX() - p1.getX();
        double yDelta = p2.getY() - p1.getY();

        if ((xDelta == 0) && (yDelta == 0)) {
            // p1 and p2 are the same point
            return point.distance(p1);
        }

        double u = ((point.getX() - p1.getX()) * xDelta + (point.getY() - p1.getY()) * yDelta) / (xDelta * xDelta
            + yDelta * yDelta);

        if (u < 0) {
            // Closest point is p1
            return point.distance(p1);
        } else if (u > 1) {
            // Closest point is p2
            return point.distance(p2);
        } else {
            // Closest point is on the line segment between p1 and p2
            double closestX = p1.getX() + u * xDelta;
            double closestY = p1.getY() + u * yDelta;
            return point.distance(closestX, closestY);
        }
    }


    public void deletePoint(ElementScalerBlock scalerBlock) {
        int index = scalers.indexOf(scalerBlock);
        getEdgePoints().remove(index);
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
        getDecoratorTarget().accept(visitor);
    }

    private void updateEdgePoints(Change<? extends Property<Point2D>> change) {
        decoratedNode.getChildren().removeAll(scalers);
        scalers.clear();

        for (int i = 0; i < change.getList().size(); i++) {
            ElementScalerBlock scalerBlock = new ElementScalerBlock(i, this, SCALER_SIZE, SCALER_SIZE);
            scalerBlock.setFill(Color.RED);

            scalers.add(scalerBlock);
            decoratedNode.getChildren().add(scalerBlock);
        }
    }
}
