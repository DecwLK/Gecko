package org.gecko.view.views.viewelement;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
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

    private static final double MIN_WIDTH = 50;
    private static final double MAX_WIDTH = 80;
    private static final Insets PADDING = new Insets(2);
    private static final CornerRadii INPUT_RADII = new CornerRadii(0, 3, 3, 0, false);
    private static final CornerRadii OUTPUT_RADII = new CornerRadii(3, 0, 0, 3, false);

    private final PortViewModel viewModel;
    private final StringProperty nameProperty;
    private final ObjectProperty<Visibility> visibilityProperty;

    public PortViewElement(PortViewModel viewModel) {
        this.viewModel = viewModel;
        this.nameProperty = new SimpleStringProperty(viewModel.getName());
        this.visibilityProperty = new SimpleObjectProperty<>(viewModel.getVisibility());
        setMinWidth(MIN_WIDTH);
        setMaxWidth(MAX_WIDTH);
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
        visibilityProperty.addListener((observable, oldValue, newValue) -> updateBackgroundColor());
        viewModel.getSystemPortOffsetProperty().bind(Bindings.createObjectBinding(() -> {
            return new Point2D(getLayoutX(), getLayoutY());
        }, layoutXProperty(), layoutYProperty()));
    }

    private void constructVisualization() {
        Label nameLabel = new Label();
        nameLabel.textProperty().bind(nameProperty);
        nameLabel.setMaxWidth(MAX_WIDTH);
        nameLabel.setPadding(PADDING);
        updateBackgroundColor();
        getChildren().add(nameLabel);
    }

    private void updateBackgroundColor() {
        boolean isInput = viewModel.getVisibility() == Visibility.INPUT;
        Background background = new Background(
            new BackgroundFill(PortViewModel.getBackgroundColor(isInput ? Visibility.OUTPUT : Visibility.INPUT),
                isInput ? INPUT_RADII : OUTPUT_RADII, null));
        setBackground(background);
    }
}
