package org.gecko.view.views.viewelement;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

/**
 * An abstract representation of a {@link Path} type view element, that is a connection in a Gecko project. Contains a
 * list of {@link Point2D path point}s.
 */
@Getter
public abstract class ConnectionViewElement extends Path {
    private static final String STYLE_CLASS = "path";
    private static final double ARROW_HEAD_LENGTH = 25;
    private static final double ARROW_HEAD_ANGLE = 10;

    private final ObservableList<Property<Point2D>> pathSource;
    private MoveTo startElement;
    @Setter
    private boolean isLoop;

    /**
     * The render path source is a list of pairs of double properties. The first element of the pair is the x property
     * of the point, and the second element is the y property of the point. This list represents the actual points that
     * are drawn on the screen. pathSource is a subset of renderPathSource. In order to draw a loop, extra points are
     * added to renderPathSource.
     */
    protected List<Pair<DoubleProperty, DoubleProperty>> renderPathSource;

    protected ConnectionViewElement(ObservableList<Property<Point2D>> path) {
        this.pathSource = path;

        updatePathVisualization();

        ListChangeListener<Property<Point2D>> pathChangedListener = change -> updatePathVisualization();
        pathSource.addListener(pathChangedListener);

        setStrokeWidth(5);
        getStyleClass().add(STYLE_CLASS);
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

    /**
     * Update the visualization of the path. Path is drawn using the path source points. Path is automatically updated
     * upon change of individual path source points.
     * <p>
     * If this connection view element is a loop, the path will be drawn as a loop by adding extra points to render path
     * source.
     */
    protected void updatePathVisualization() {
        getElements().clear();

        renderPathSource = new ArrayList<>();

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
        renderPathSource.add(new Pair<>(startElement.xProperty(), startElement.yProperty()));

        if (isLoop) {
            // If source and destination are the same, draw a loop
            double radius = Math.abs(pathSource.getFirst().getValue().subtract(pathSource.getLast().getValue()).getY());
            LineTo lineTo =
                new LineTo(pathSource.getFirst().getValue().getX() - radius, pathSource.getFirst().getValue().getY());
            lineTo.xProperty()
                .bind(Bindings.createDoubleBinding(() -> pathSource.getFirst().getValue().getX() - radius,
                    pathSource.getFirst()));
            lineTo.yProperty()
                .bind(
                    Bindings.createDoubleBinding(() -> pathSource.getFirst().getValue().getY(), pathSource.getFirst()));
            getElements().add(lineTo);
            renderPathSource.add(new Pair<>(lineTo.xProperty(), lineTo.yProperty()));

            LineTo lineTo2 =
                new LineTo(pathSource.getFirst().getValue().getX(), pathSource.getFirst().getValue().getY() + radius);
            lineTo2.xProperty()
                .bind(
                    Bindings.createDoubleBinding(() -> pathSource.getFirst().getValue().getX(), pathSource.getFirst()));
            lineTo2.yProperty()
                .bind(Bindings.createDoubleBinding(() -> pathSource.getFirst().getValue().getY() + radius,
                    pathSource.getFirst()));
            getElements().add(lineTo2);
            renderPathSource.add(new Pair<>(lineTo2.xProperty(), lineTo2.yProperty()));

            LineTo lineTo3 =
                new LineTo(pathSource.getFirst().getValue().getX(), pathSource.getFirst().getValue().getY() + radius);
            lineTo2.xProperty()
                .bind(Bindings.createDoubleBinding(() -> pathSource.getLast().getValue().getX() - radius,
                    pathSource.getFirst()));
            lineTo2.yProperty()
                .bind(
                    Bindings.createDoubleBinding(() -> pathSource.getLast().getValue().getY(), pathSource.getFirst()));
            getElements().add(lineTo3);
            renderPathSource.add(new Pair<>(lineTo3.xProperty(), lineTo3.yProperty()));

            LineTo endPoint =
                new LineTo(pathSource.getLast().getValue().getX(), pathSource.getLast().getValue().getY());
            endPoint.xProperty()
                .bind(Bindings.createDoubleBinding(() -> pathSource.getLast().getValue().getX(), pathSource.getLast()));
            endPoint.yProperty()
                .bind(Bindings.createDoubleBinding(() -> pathSource.getLast().getValue().getY(), pathSource.getLast()));
            getElements().add(endPoint);
        } else {
            // Elements in the middle
            for (Property<Point2D> point : pathSource) {
                LineTo lineTo = new LineTo(point.getValue().getX(), point.getValue().getY());
                lineTo.xProperty().bind(Bindings.createDoubleBinding(() -> point.getValue().getX(), point));
                lineTo.yProperty().bind(Bindings.createDoubleBinding(() -> point.getValue().getY(), point));
                getElements().add(lineTo);
                renderPathSource.add(new Pair<>(lineTo.xProperty(), lineTo.yProperty()));
            }
        }

        DoubleProperty lastX = renderPathSource.getLast().getKey();
        DoubleProperty lastY = renderPathSource.getLast().getValue();
        DoubleProperty secondLastX = renderPathSource.get(renderPathSource.size() - 2).getKey();
        DoubleProperty secondLastY = renderPathSource.get(renderPathSource.size() - 2).getValue();

        // Arrow head
        MoveTo arrowHeadUp = new MoveTo(pathSource.getLast().getValue().getX(), pathSource.getLast().getValue().getY());
        arrowHeadUp.xProperty()
            .bind(Bindings.createDoubleBinding(() -> pathSource.getLast().getValue().getX(), pathSource.getLast(),
                pathSource.getFirst()));
        arrowHeadUp.yProperty()
            .bind(Bindings.createDoubleBinding(() -> pathSource.getLast().getValue().getY(), pathSource.getLast(),
                pathSource.getFirst()));
        getElements().add(arrowHeadUp);

        Point2D arrowHeadUpPoint =
            calculateArrowHeadPosition(new Point2D(secondLastX.getValue(), secondLastY.getValue()),
                new Point2D(lastX.getValue(), lastY.getValue()), ARROW_HEAD_ANGLE);
        LineTo arrowHeadUpLine = new LineTo(arrowHeadUpPoint.getX(), arrowHeadUpPoint.getY());
        arrowHeadUpLine.xProperty()
            .bind(Bindings.createDoubleBinding(
                () -> calculateArrowHeadPosition(new Point2D(secondLastX.getValue(), secondLastY.getValue()),
                    new Point2D(lastX.getValue(), lastY.getValue()), ARROW_HEAD_ANGLE).getX(), pathSource.getLast(),
                pathSource.getFirst()));
        arrowHeadUpLine.yProperty()
            .bind(Bindings.createDoubleBinding(
                () -> calculateArrowHeadPosition(new Point2D(secondLastX.getValue(), secondLastY.getValue()),
                    new Point2D(lastX.getValue(), lastY.getValue()), ARROW_HEAD_ANGLE).getY(), pathSource.getLast(),
                pathSource.getFirst()));
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

        Point2D arrowHeadDownPoint =
            calculateArrowHeadPosition(new Point2D(secondLastX.getValue(), secondLastY.getValue()),
                new Point2D(lastX.getValue(), lastY.getValue()), -ARROW_HEAD_ANGLE);
        LineTo arrowHeadDownLine = new LineTo(arrowHeadDownPoint.getX(), arrowHeadDownPoint.getY());
        arrowHeadDownLine.xProperty()
            .bind(Bindings.createDoubleBinding(
                () -> calculateArrowHeadPosition(new Point2D(secondLastX.getValue(), secondLastY.getValue()),
                    new Point2D(lastX.getValue(), lastY.getValue()), -ARROW_HEAD_ANGLE).getX(), pathSource.getLast(),
                pathSource.getFirst()));
        arrowHeadDownLine.yProperty()
            .bind(Bindings.createDoubleBinding(
                () -> calculateArrowHeadPosition(new Point2D(secondLastX.getValue(), secondLastY.getValue()),
                    new Point2D(lastX.getValue(), lastY.getValue()), -ARROW_HEAD_ANGLE).getY(), pathSource.getLast(),
                pathSource.getFirst()));
        getElements().add(arrowHeadDownLine);
    }

    private Point2D calculateArrowHeadPosition(Point2D start, Point2D end, double offset) {
        Point2D vector = end.subtract(start).normalize();
        Point2D orthogonalVector = new Point2D(-vector.getY(), vector.getX()).normalize();

        // Arrow head position is calculated by moving from the last point in the path (that is orthogonally shifted) by
        // offset to the first point in the path
        Point2D arrowHeadPosition = end.add(orthogonalVector.multiply(offset));
        return arrowHeadPosition.subtract(vector.multiply(ARROW_HEAD_LENGTH));
    }
}
