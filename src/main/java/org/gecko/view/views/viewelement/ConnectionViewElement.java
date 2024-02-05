package org.gecko.view.views.viewelement;

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

    private MoveTo startElement;

    private final ObservableList<Property<Point2D>> pathSource;

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
    }
}
