package org.gecko.view.views.viewelement;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import lombok.Getter;

@Getter
public abstract class ConnectionViewElement extends Path {

    private static final double EDGE_DIST_OFFSET = 20;
    private static final double ARROW_HEAD_LENGTH = 25;
    private static final double ARROW_HEAD_ANGLE = 10;

    private final ObservableList<Property<Point2D>> pathSource;
    private MoveTo startElement;

    protected ConnectionViewElement(ObservableList<Property<Point2D>> path) {
        this.pathSource = path;

        updatePathVisualization();

        ListChangeListener<Property<Point2D>> pathChangedListener = change -> updatePathVisualization();
        pathSource.addListener(pathChangedListener);

        setStrokeWidth(5);
    }

    protected void updatePathSource(int index, Point2D point) {
        pathSource.get(index).setValue(point);
    }

    /**
     * Mask the end position of the block with the given size and position. The mask is calculated by the intersection
     * of the block and the line defined by the start and end points.
     *
     * @param blockPosition The position of the block
     * @param blockSize     The size of the block
     * @param start         The start point of the line
     * @param end           The end point of the line
     * @param edgeOffset    The offset to apply to the intersection point
     * @return The masked position of the block (end point)
     */
    protected Point2D maskBlock(
        Point2D blockPosition, Point2D blockSize, Point2D start, Point2D end, double edgeOffset) {
        List<Point2D> blockCorners = new ArrayList<>();
        blockCorners.add(blockPosition);
        blockCorners.add(blockPosition.add(new Point2D(blockSize.getX(), 0)));
        blockCorners.add(blockPosition.add(new Point2D(blockSize.getX(), blockSize.getY())));
        blockCorners.add(blockPosition.add(new Point2D(0, blockSize.getY())));

        // loop through the block corners and find the intersection point with the line
        Point2D intersection = null;

        Line path = new Line(start.getX(), start.getY(), end.getX(), end.getY());
        for (int i = 0; i < blockCorners.size(); i++) {
            Point2D corner = blockCorners.get(i);
            Point2D nextCorner = blockCorners.get((i + 1) % blockCorners.size());

            Line border = new Line(corner.getX(), corner.getY(), nextCorner.getX(), nextCorner.getY());
            Shape intersectionShape = Shape.intersect(path, border);

            if (intersectionShape.getBoundsInLocal().getMaxX() <= 0
                || intersectionShape.getBoundsInLocal().getMaxY() <= 0) {
                // No intersection
                continue;
            }
            Point2D vector = corner.subtract(nextCorner).normalize();

            Point2D edgePosition = new Point2D(intersectionShape.getBoundsInLocal().getMinX(),
                intersectionShape.getBoundsInLocal().getMinY());

            if (edgeOffset <= -1) {
                intersection = edgePosition;
            } else {
                intersection = corner.interpolate(nextCorner, edgeOffset);
            }
            break;
        }

        return intersection;
    }

    private void updatePathVisualization() {
        getElements().clear();

        // If there are less than two points, there is no path to draw
        if (pathSource.size() < 2) {
            return;
        }

        // Start element
        startElement = new MoveTo(pathSource.getFirst().getValue().getX(), pathSource.getFirst().getValue().getY());
        startElement.xProperty()
            .bind(Bindings.createDoubleBinding(() -> pathSource.getFirst().getValue().getX(), pathSource.getFirst()));
        startElement.yProperty()
            .bind(Bindings.createDoubleBinding(() -> pathSource.getFirst().getValue().getY(), pathSource.getFirst()));
        getElements().add(startElement);

        // Elements in the middle
        for (Property<Point2D> point : pathSource) {
            LineTo lineTo = new LineTo(point.getValue().getX(), point.getValue().getY());
            lineTo.xProperty().bind(Bindings.createDoubleBinding(() -> point.getValue().getX(), point));
            lineTo.yProperty().bind(Bindings.createDoubleBinding(() -> point.getValue().getY(), point));
            getElements().add(lineTo);
        }

        // Arrow head
        MoveTo arrowHeadUp = new MoveTo(pathSource.getLast().getValue().getX(), pathSource.getLast().getValue().getY());
        arrowHeadUp.xProperty()
            .bind(Bindings.createDoubleBinding(() -> pathSource.getLast().getValue().getX(), pathSource.getLast(),
                pathSource.getFirst()));
        arrowHeadUp.yProperty()
            .bind(Bindings.createDoubleBinding(() -> pathSource.getLast().getValue().getY(), pathSource.getLast(),
                pathSource.getFirst()));
        getElements().add(arrowHeadUp);

        Point2D arrowHeadUpPoint = calculateArrowHeadPosition(ARROW_HEAD_ANGLE);
        LineTo arrowHeadUpLine = new LineTo(arrowHeadUpPoint.getX(), arrowHeadUpPoint.getY());
        arrowHeadUpLine.xProperty()
            .bind(Bindings.createDoubleBinding(() -> calculateArrowHeadPosition(ARROW_HEAD_ANGLE).getX(),
                pathSource.getLast(), pathSource.getFirst()));
        arrowHeadUpLine.yProperty()
            .bind(Bindings.createDoubleBinding(() -> calculateArrowHeadPosition(ARROW_HEAD_ANGLE).getY(),
                pathSource.getLast(), pathSource.getFirst()));
        getElements().add(arrowHeadUpLine);

        MoveTo arrowHeadDown =
            new MoveTo(pathSource.getLast().getValue().getX(), pathSource.getLast().getValue().getY());
        arrowHeadDown.xProperty()
            .bind(Bindings.createDoubleBinding(() -> pathSource.getLast().getValue().getX(), pathSource.getLast(),
                pathSource.getFirst()));
        arrowHeadDown.yProperty()
            .bind(Bindings.createDoubleBinding(() -> pathSource.getLast().getValue().getY(), pathSource.getLast(),
                pathSource.getFirst()));
        getElements().add(arrowHeadDown);

        Point2D arrowHeadDownPoint = calculateArrowHeadPosition(-ARROW_HEAD_ANGLE);
        LineTo arrowHeadDownLine = new LineTo(arrowHeadDownPoint.getX(), arrowHeadDownPoint.getY());
        arrowHeadDownLine.xProperty()
            .bind(Bindings.createDoubleBinding(() -> calculateArrowHeadPosition(-ARROW_HEAD_ANGLE).getX(),
                pathSource.getLast(), pathSource.getFirst()));
        arrowHeadDownLine.yProperty()
            .bind(Bindings.createDoubleBinding(() -> calculateArrowHeadPosition(-ARROW_HEAD_ANGLE).getY(),
                pathSource.getLast(), pathSource.getFirst()));
        getElements().add(arrowHeadDownLine);
    }

    private Point2D calculateArrowHeadPosition(double offset) {
        Point2D vector = pathSource.getLast().getValue().subtract(pathSource.getFirst().getValue()).normalize();
        Point2D orthogonalVector = new Point2D(-vector.getY(), vector.getX()).normalize();

        // Arrow head position is calculated by moving from the last point in the path (that is orthogonally shifted) by
        // offset to the first point in the path
        Point2D arrowHeadPosition = pathSource.getLast().getValue().add(orthogonalVector.multiply(offset));
        return arrowHeadPosition.subtract(vector.multiply(ARROW_HEAD_LENGTH));
    }
}
