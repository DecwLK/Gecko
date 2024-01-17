package org.gecko.view.views.viewelement;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.gecko.model.Visibility;
import org.gecko.viewmodel.PortViewModel;

public class VariableBlockViewElement extends Pane implements ViewElement<PortViewModel> {

    private final PortViewModel portViewModel;
    private final StringProperty nameProperty;
    private final StringProperty typeProperty;
    private final Property<Visibility> visibilityProperty;

    public VariableBlockViewElement(PortViewModel portViewModel) {
        this.nameProperty = new SimpleStringProperty();
        this.typeProperty = new SimpleStringProperty();
        this.visibilityProperty = new SimpleObjectProperty<>();
        this.portViewModel = portViewModel;
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

    private void bindTo() {
        nameProperty.bind(portViewModel.getNameProperty());
        typeProperty.bind(portViewModel.getTypeProperty());
        visibilityProperty.bind(portViewModel.getVisibilityProperty());
        layoutXProperty().bind(Bindings.createDoubleBinding(() -> portViewModel.getPosition().getX(), portViewModel.getPositionProperty()));
        layoutYProperty().bind(Bindings.createDoubleBinding(() -> portViewModel.getPosition().getY(), portViewModel.getPositionProperty()));
        prefWidthProperty().bind(Bindings.createDoubleBinding(() -> portViewModel.getSize().getX(), portViewModel.getSizeProperty()));
        prefHeightProperty().bind(Bindings.createDoubleBinding(() -> portViewModel.getSize().getY(), portViewModel.getSizeProperty()));
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
    }
}
