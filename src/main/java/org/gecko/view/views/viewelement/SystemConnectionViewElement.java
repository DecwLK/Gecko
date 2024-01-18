package org.gecko.view.views.viewelement;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import lombok.Getter;
import org.gecko.model.Visibility;
import org.gecko.viewmodel.SystemConnectionViewModel;

@Getter
public class SystemConnectionViewElement extends ConnectionViewElement implements ViewElement<SystemConnectionViewModel> {

    private final SystemConnectionViewModel systemConnectionViewModel;
    private final Property<Visibility> visibilityProperty;
    private final StringProperty typeProperty;
    //TODO source and destination properties

    public SystemConnectionViewElement(SystemConnectionViewModel systemConnectionViewModel) {
        this.visibilityProperty = new SimpleObjectProperty<>();
        this.typeProperty = new SimpleStringProperty();
        this.systemConnectionViewModel = systemConnectionViewModel;
        bindViewModel();
        constructVisualization();
        //TODO source and destination properties
    }

    @Override
    public Node drawElement() {
        return this;
    }

    @Override
    public SystemConnectionViewModel getTarget() {
        return systemConnectionViewModel;
    }

    @Override
    public Point2D getPosition() {
        return systemConnectionViewModel.getPosition();
    }

    private void bindViewModel() {
        startXProperty().bind(Bindings.createDoubleBinding(() -> systemConnectionViewModel.getSource().getPosition().getX(),
            systemConnectionViewModel.getSource().getPositionProperty()));
        startYProperty().bind(Bindings.createDoubleBinding(() -> systemConnectionViewModel.getSource().getPosition().getY(),
            systemConnectionViewModel.getSource().getPositionProperty()));
        endXProperty().bind(Bindings.createDoubleBinding(() -> systemConnectionViewModel.getDestination().getSize().getX(),
            systemConnectionViewModel.getDestination().getSizeProperty()));
        endYProperty().bind(Bindings.createDoubleBinding(() -> systemConnectionViewModel.getDestination().getSize().getY(),
            systemConnectionViewModel.getDestination().getSizeProperty()));
        //TODO source + destination properties
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
    }

    private void constructVisualization() {
        setStroke(Color.BLACK);
    }
}
