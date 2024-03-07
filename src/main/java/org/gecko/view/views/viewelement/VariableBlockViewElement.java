package org.gecko.view.views.viewelement;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import lombok.AccessLevel;
import lombok.Getter;
import org.gecko.model.Visibility;
import org.gecko.viewmodel.PortViewModel;

/**
 * Represents a type of {@link BlockViewElement} implementing the {@link ViewElement} interface, which encapsulates an
 * {@link PortViewModel}.
 */
@Getter
public class VariableBlockViewElement extends BlockViewElement implements ViewElement<PortViewModel> {

    private static final int Z_PRIORITY = 30;

    @Getter(AccessLevel.NONE)
    private final PortViewModel portViewModel;
    private final StringProperty nameProperty;
    private final StringProperty typeProperty;
    private final Property<Visibility> visibilityProperty;

    public VariableBlockViewElement(PortViewModel portViewModel) {
        super(portViewModel);
        this.nameProperty = new SimpleStringProperty();
        this.typeProperty = new SimpleStringProperty();
        this.visibilityProperty = new SimpleObjectProperty<>();
        this.portViewModel = portViewModel;
        bindViewModel();
        constructVisualization();
    }

    @Override
    public Node drawElement() {
        return this;
    }

    @Override
    public PortViewModel getTarget() {
        return portViewModel;
    }

    @Override
    public Point2D getPosition() {
        return portViewModel.getPosition();
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getZPriority() {
        return Z_PRIORITY;
    }

    private void bindViewModel() {
        nameProperty.bind(portViewModel.getNameProperty());
        typeProperty.bind(portViewModel.getTypeProperty());
        visibilityProperty.bind(Bindings.createObjectBinding(() -> switch (portViewModel.getVisibility()) {
            //Swap output and input because we are inside the system
            case INPUT -> Visibility.OUTPUT;
            case OUTPUT -> Visibility.INPUT;
            case STATE -> Visibility.STATE;
        }, portViewModel.getVisibilityProperty()));
        prefWidthProperty().bind(
            Bindings.createDoubleBinding(() -> portViewModel.getSize().getX(), portViewModel.getSizeProperty()));
        prefHeightProperty().bind(
            Bindings.createDoubleBinding(() -> portViewModel.getSize().getY(), portViewModel.getSizeProperty()));
    }

    private void constructVisualization() {
        StackPane container = new StackPane();
        Rectangle rectangle = new Rectangle();
        rectangle.widthProperty().bind(widthProperty());
        rectangle.heightProperty().bind(heightProperty());
        rectangle.fillProperty()
            .bind(Bindings.createObjectBinding(portViewModel::getBackgroundColor, visibilityProperty));
        rectangle.setArcWidth(BACKGROUND_ROUNDING);
        rectangle.setArcHeight(BACKGROUND_ROUNDING);
        container.getChildren().add(rectangle);
        Label label = new Label();
        label.textProperty().bind(nameProperty);
        container.getChildren().add(label);
        getChildren().add(container);
    }
}
