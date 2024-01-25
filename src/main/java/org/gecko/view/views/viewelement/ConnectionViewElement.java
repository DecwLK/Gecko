package org.gecko.view.views.viewelement;

import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.geometry.Point2D;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import lombok.Getter;

@Getter
public abstract class ConnectionViewElement extends Path {

    private final MoveTo startElement;
    private final LineTo endElement;

    protected ConnectionViewElement(List<Property<Point2D>> pathPointSource) {
        final List<Property<Point2D>> path = pathPointSource;

        // Start element
        startElement = new MoveTo(path.getFirst().getValue().getX(), path.getFirst().getValue().getY());
        startElement.xProperty()
            .bind(Bindings.createDoubleBinding(() -> path.getFirst().getValue().getX(), path.getFirst()));
        startElement.yProperty()
            .bind(Bindings.createDoubleBinding(() -> path.getFirst().getValue().getY(), path.getFirst()));
        getElements().add(startElement);

        // Elements in the middle
        for (Property<Point2D> point : path) {
            LineTo lineTo = new LineTo(point.getValue().getX(), point.getValue().getY());
            lineTo.xProperty().bind(Bindings.createDoubleBinding(() -> point.getValue().getX(), point));
            lineTo.yProperty().bind(Bindings.createDoubleBinding(() -> point.getValue().getY(), point));
            getElements().add(lineTo);
        }

        // End element
        endElement = new LineTo();
        endElement.xProperty()
            .bind(Bindings.createDoubleBinding(() -> path.getLast().getValue().getX(), path.getLast()));
        endElement.yProperty()
            .bind(Bindings.createDoubleBinding(() -> path.getLast().getValue().getY(), path.getLast()));
        getElements().add(endElement);

        setStrokeWidth(5);
    }
}
