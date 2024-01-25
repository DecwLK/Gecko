package org.gecko.view.views.viewelement;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import lombok.Getter;
import org.gecko.model.Element;
import org.gecko.viewmodel.PositionableViewModelElement;

public abstract class BlockViewElement extends Pane {

    @Getter
    private final List<Property<Point2D>> edgePoints;

    protected BlockViewElement(PositionableViewModelElement<? extends Element> positionableViewModelElement) {
        // Initialize edge points for a rectangular shaped block
        this.edgePoints = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            edgePoints.add(new SimpleObjectProperty<>(new Point2D(0, 0)));
        }

        // Auto calculate new edge points on size and position changes
        positionableViewModelElement.getSizeProperty().addListener((observable, oldValue, newValue) -> {
            calculateEdgePoints(positionableViewModelElement);
        });
        positionableViewModelElement.getPositionProperty().addListener((observable, oldValue, newValue) -> {
            calculateEdgePoints(positionableViewModelElement);
        });

        calculateEdgePoints(positionableViewModelElement);
    }

    protected void calculateEdgePoints(PositionableViewModelElement<?> target) {
        double x = target.getSize().getX();
        double y = target.getSize().getY();
        edgePoints.get(0).setValue(new Point2D(0, 0));
        edgePoints.get(1).setValue(new Point2D(x, 0));
        edgePoints.get(2).setValue(new Point2D(x, y));
        edgePoints.get(3).setValue(new Point2D(0, y));
    }
}