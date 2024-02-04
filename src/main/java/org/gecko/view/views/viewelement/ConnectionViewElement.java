package org.gecko.view.views.viewelement;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import lombok.Getter;

@Getter
public abstract class ConnectionViewElement extends Path {

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
     * @return The masked position of the block (end point)
     */
    protected Point2D maskBlock(Point2D blockPosition, Point2D blockSize, Point2D start, Point2D end) {
        List<Point2D> blockCorners = new ArrayList<>();
        blockCorners.add(blockPosition);
        blockCorners.add(blockPosition.add(new Point2D(blockSize.getX(), 0)));
        blockCorners.add(blockPosition.add(new Point2D(blockSize.getX(), blockSize.getY())));
        blockCorners.add(blockPosition.add(new Point2D(0, blockSize.getY())));

        // loop through the block corners and find the intersection point with the line
        Point2D intersection = null;

        for (int i = 0; i < blockCorners.size(); i++) {
            Point2D corner = blockCorners.get(i);
            Point2D nextCorner = blockCorners.get((i + 1) % blockCorners.size());

            Point2D intersectionPoint = getIntersectionPoint(start, end, corner, nextCorner);
            if (intersectionPoint != null) {
                // if the intersection point is closer to the start point than the current intersection, update the
                // intersection
                if (intersection == null || intersection.distance(start) > intersectionPoint.distance(start)) {
                    intersection = intersectionPoint;
                }
            }
        }

        return intersection;
    }

    private Point2D getIntersectionPoint(Point2D x0, Point2D x1, Point2D y0, Point2D y1) {
        double x0x1 = x0.getX() - x1.getX();
        double y0y1 = x0.getY() - x1.getY();
        double x0y1 = x0.getX() * x1.getY() - x0.getY() * x1.getX();

        double y0y1_ = y0.getY() - y1.getY();
        double x0x1_ = y0.getX() - y1.getX();
        double y0x1_ = y0.getX() * y1.getY() - y0.getY() * y1.getX();

        double det = x0x1 * y0y1_ - y0y1 * x0x1_;
        if (det == 0) {
            return null;
        }

        // check if the intersection point is within the line segments
        if (Math.min(x0.getX(), x1.getX()) > Math.max(y0.getX(), y1.getX()) ||
            Math.max(x0.getX(), x1.getX()) < Math.min(y0.getX(), y1.getX()) ||
            Math.min(x0.getY(), x1.getY()) > Math.max(y0.getY(), y1.getY()) ||
            Math.max(x0.getY(), x1.getY()) < Math.min(y0.getY(), y1.getY())) {
            return null;
        }

        double x = (x0y1 * x0x1_ - x0x1 * y0x1_) / det;
        double y = (x0y1 * y0y1_ - y0y1 * y0x1_) / det;

        return new Point2D(x, y);
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
