package org.gecko.view.views.viewelement;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

/**
 * An abstract representation of a {@link Path} type view element, that is a connection in a Gecko project. Contains a
 * list of {@link Point2D path point}s.
 */
@Getter
public abstract class ConnectionViewElement extends Path {

    private final ObservableList<Property<Point2D>> pathSource;
    private MoveTo startElement;
    protected BooleanProperty isLoopProperty;
    protected IntegerProperty orientationProperty;


    @Getter
    @Setter
    private boolean selected;

    /**
     * The first element of the pair is the x property of the point, and the second element is the y property of the
     * point. This list represents the actual points that are drawn on the screen. pathSource is a subset of
     * renderPathSource. In order to draw a loop, extra points are added to renderPathSource.
     */
    protected List<Pair<DoubleProperty, DoubleProperty>> renderPathSource;

    private static final int STROKE_WIDTH = 5;
    private static final String STYLE_CLASS = "path";
    private static final double ARROW_HEAD_LENGTH = 25;
    private static final double ARROW_HEAD_ANGLE = 10;
    private static final Point2D ANGLE_OFFSET_HELPER = new Point2D(50, 10);
    private static final int MIN_REQUIRED_PATH_POINTS = 2;

    protected ConnectionViewElement(ObservableList<Property<Point2D>> path) {
        this.pathSource = path;
        this.isLoopProperty = new SimpleBooleanProperty(false);
        this.orientationProperty = new SimpleIntegerProperty();

        updatePathVisualization();

        ListChangeListener<Property<Point2D>> pathChangedListener = change -> updatePathVisualization();
        pathSource.addListener(pathChangedListener);
        isLoopProperty.addListener((observable, oldValue, newValue) -> updatePathVisualization());
        orientationProperty.addListener((observable, oldValue, newValue) -> updatePathVisualization());

        setStrokeWidth(STROKE_WIDTH);
        getStyleClass().add(STYLE_CLASS);
    }


    /**
     * Update the visualization of the path. Path is drawn using the path source points. Path is automatically updated
     * upon change of individual path source points. If this connection view element is a loop, the path will be drawn
     * as a loop by adding extra points to render path source.
     */
    protected void updatePathVisualization() {
        getElements().clear();

        renderPathSource = new ArrayList<>();

        // If there are less than two points, there is no path to draw
        if (pathSource.size() < MIN_REQUIRED_PATH_POINTS) {
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

        if (isLoopProperty.get()) {
            // If source and destination are the same, draw a loop
            ArcTo arcTo = new ArcTo();
            arcTo.radiusXProperty()
                .bind(Bindings.createDoubleBinding(
                    () -> Math.abs(pathSource.getFirst().getValue().getX() - pathSource.getLast().getValue().getX()),
                    pathSource.getFirst(), pathSource.getLast()));
            arcTo.radiusYProperty()
                .bind(Bindings.createDoubleBinding(
                    () -> Math.abs(pathSource.getFirst().getValue().getY() - pathSource.getLast().getValue().getY()),
                    pathSource.getFirst(), pathSource.getLast()));
            arcTo.xProperty()
                .bind(Bindings.createDoubleBinding(() -> pathSource.getLast().getValue().getX(), pathSource.getLast()));
            arcTo.yProperty()
                .bind(Bindings.createDoubleBinding(() -> pathSource.getLast().getValue().getY(), pathSource.getLast()));
            arcTo.largeArcFlagProperty().setValue(true);
            arcTo.sweepFlagProperty().setValue(true);
            getElements().add(arcTo);
            renderPathSource.add(new Pair<>(arcTo.xProperty(), arcTo.yProperty()));
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

        final DoubleProperty secondLastX = new SimpleDoubleProperty();
        secondLastX.bind(renderPathSource.get(renderPathSource.size() - 2).getKey());
        final DoubleProperty secondLastY = new SimpleDoubleProperty();
        secondLastY.bind(renderPathSource.get(renderPathSource.size() - 2).getValue());

        if (isLoopProperty.get()) {
            secondLastY.unbind();
            secondLastX.unbind();
            switch (orientationProperty.get()) {
                case 0:
                    secondLastX.bind(lastX.add(ANGLE_OFFSET_HELPER.getX()));
                    secondLastY.bind(lastY.subtract(ANGLE_OFFSET_HELPER.getY()));
                    break;
                case 1:
                    secondLastX.bind(lastX.add(ANGLE_OFFSET_HELPER.getY()));
                    secondLastY.bind(lastY.add(ANGLE_OFFSET_HELPER.getX()));
                    break;
                case 2:
                    secondLastX.bind(lastX.subtract(ANGLE_OFFSET_HELPER.getX()));
                    secondLastY.bind(lastY.add(ANGLE_OFFSET_HELPER.getY()));
                    break;
                case 3:
                    secondLastX.bind(lastX.subtract(ANGLE_OFFSET_HELPER.getY()));
                    secondLastY.bind(lastY.subtract(ANGLE_OFFSET_HELPER.getX()));
                    break;
                default:
                    break;
            }
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
