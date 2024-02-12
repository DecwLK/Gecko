package org.gecko.view.views.viewelement;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import lombok.Getter;
import org.gecko.model.Element;
import org.gecko.viewmodel.PositionableViewModelElement;

/**
 * An abstract representation of a {@link Pane} view element, that is an element with a rectangular shape in a Gecko
 * project. Contains a list of {@link Point2D edge point}s.
 */
public abstract class BlockViewElement extends Pane {

    @Getter
    private final ObservableList<Property<Point2D>> edgePoints;

    protected static final int BACKGROUND_ROUNDING = 15;

    protected BlockViewElement(PositionableViewModelElement<? extends Element> positionableViewModelElement) {
        // Initialize edge points for a rectangular shaped block
        this.edgePoints = FXCollections.observableArrayList();
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

    private void calculateEdgePoints(PositionableViewModelElement<?> target) {
        Point2D position = target.getPosition();
        double width = target.getSize().getX();
        double height = target.getSize().getY();
        edgePoints.get(0).setValue(position.add(new Point2D(0, 0)));
        edgePoints.get(1).setValue(position.add(new Point2D(width, 0)));
        edgePoints.get(2).setValue(position.add(new Point2D(width, height)));
        edgePoints.get(3).setValue(position.add(new Point2D(0, height)));
    }
}
