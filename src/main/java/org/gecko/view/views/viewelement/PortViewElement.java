package org.gecko.view.views.viewelement;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.Getter;
import org.gecko.model.Visibility;
import org.gecko.viewmodel.PortViewModel;

/**
 * Represents a type of a {@link Pane} used for the visualization of a {@link PortViewModel}, to which it holds a
 * reference, along with its {@link String name} and {@link Visibility}.
 */
@Getter
public class PortViewElement extends Pane {

    private final PortViewModel viewModel;
    private final StringProperty nameProperty;
    private final ObjectProperty<Visibility> visibilityProperty;

    public PortViewElement(PortViewModel viewModel) {
        this.viewModel = viewModel;
        this.nameProperty = new SimpleStringProperty(viewModel.getName());
        this.visibilityProperty = new SimpleObjectProperty<>(viewModel.getVisibility());

        bindToViewModel();
        constructVisualization();
    }

    /**
     * Returns the position of the port view element.
     *
     * @return the position of the port view element
     */
    public Point2D getViewPosition() {
        return new Point2D(getLayoutX(), getLayoutY());
    }

    /**
     * Returns the size of the port view element.
     *
     * @return the size of the port view element
     */
    public Point2D getViewSize() {
        return new Point2D(getWidth(), getHeight());
    }

    private void bindToViewModel() {
        nameProperty.bind(viewModel.getNameProperty());
        visibilityProperty.bind(viewModel.getVisibilityProperty());
    }

    private void constructVisualization() {
        Label nameLabel = new Label();
        nameLabel.textProperty().bind(nameProperty);
        getChildren().add(nameLabel);
    }
}
