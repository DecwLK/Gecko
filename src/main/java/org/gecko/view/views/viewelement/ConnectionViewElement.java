package org.gecko.view.views.viewelement;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
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

        ListChangeListener<Property<Point2D>> pathChangedListener = change -> updatePathVisualization();
        pathSource.addListener(pathChangedListener);

        updatePathVisualization();

        setStrokeWidth(5);
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
            .bind(Bindings.createDoubleBinding(() -> pathSource.getLast().getValue().getX(), pathSource.getLast()));
        arrowHeadUp.yProperty()
            .bind(Bindings.createDoubleBinding(() -> pathSource.getLast().getValue().getY(), pathSource.getLast()));
        getElements().add(arrowHeadUp);

        Point2D arrowHeadUpPoint = calculateArrowHeadPosition(ARROW_HEAD_ANGLE);
        LineTo arrowHeadUpLine = new LineTo(arrowHeadUpPoint.getX(), arrowHeadUpPoint.getY());
        arrowHeadUpLine.xProperty()
            .bind(Bindings.createDoubleBinding(() -> calculateArrowHeadPosition(ARROW_HEAD_ANGLE).getX(),
                pathSource.getLast()));
        arrowHeadUpLine.yProperty()
            .bind(Bindings.createDoubleBinding(() -> calculateArrowHeadPosition(ARROW_HEAD_ANGLE).getY(),
                pathSource.getLast()));
        getElements().add(arrowHeadUpLine);

        MoveTo arrowHeadDown =
            new MoveTo(pathSource.getLast().getValue().getX(), pathSource.getLast().getValue().getY());
        arrowHeadDown.xProperty()
            .bind(Bindings.createDoubleBinding(() -> pathSource.getLast().getValue().getX(), pathSource.getLast()));
        arrowHeadDown.yProperty()
            .bind(Bindings.createDoubleBinding(() -> pathSource.getLast().getValue().getY(), pathSource.getLast()));
        getElements().add(arrowHeadDown);

        Point2D arrowHeadDownPoint = calculateArrowHeadPosition(-ARROW_HEAD_ANGLE);
        LineTo arrowHeadDownLine = new LineTo(arrowHeadDownPoint.getX(), arrowHeadDownPoint.getY());
        arrowHeadDownLine.xProperty()
            .bind(Bindings.createDoubleBinding(() -> calculateArrowHeadPosition(-ARROW_HEAD_ANGLE).getX(),
                pathSource.getLast()));
        arrowHeadDownLine.yProperty()
            .bind(Bindings.createDoubleBinding(() -> calculateArrowHeadPosition(-ARROW_HEAD_ANGLE).getY(),
                pathSource.getLast()));
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
