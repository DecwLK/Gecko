package org.gecko.view.views.viewelement;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import lombok.AccessLevel;
import lombok.Getter;
import org.gecko.model.Visibility;
import org.gecko.viewmodel.SystemConnectionViewModel;

@Getter
public class SystemConnectionViewElement extends Line implements ViewElement<SystemConnectionViewModel> {

    private static final int Z_PRIORITY = 20;

    @Getter(AccessLevel.NONE)
    private final SystemConnectionViewModel systemConnectionViewModel;
    private final Property<Visibility> visibilityProperty;
    private final StringProperty typeProperty;

    public SystemConnectionViewElement(SystemConnectionViewModel systemConnectionViewModel) {
        //super(systemConnectionViewModel.getEdgePoints());
        this.visibilityProperty = new SimpleObjectProperty<>();
        this.typeProperty = new SimpleStringProperty();
        this.systemConnectionViewModel = systemConnectionViewModel;
        bindViewModel();
        constructVisualization();
    }

    @Override
    public Node drawElement() {
        return this;
    }

    @Override
    public ObservableList<Property<Point2D>> getEdgePoints() {
        return systemConnectionViewModel.getEdgePoints();
    }

    @Override
    public void setEdgePoint(int index, Point2D point) {
    }

    @Override
    public SystemConnectionViewModel getTarget() {
        return systemConnectionViewModel;
    }

    @Override
    public Point2D getPosition() {
        return systemConnectionViewModel.getPosition();
    }

    @Override
    public int getZPriority() {
        return Z_PRIORITY;
    }

    private void bindViewModel() {
        startXProperty().bind(
            Bindings.createDoubleBinding(() -> systemConnectionViewModel.getSource().getPosition().getX(),
                systemConnectionViewModel.getSource().getPositionProperty()));
        startYProperty().bind(
            Bindings.createDoubleBinding(() -> systemConnectionViewModel.getSource().getPosition().getY(),
                systemConnectionViewModel.getSource().getPositionProperty()));
        endXProperty().bind(
            Bindings.createDoubleBinding(() -> systemConnectionViewModel.getDestination().getSize().getX(),
                systemConnectionViewModel.getDestination().getPositionProperty()));
        endYProperty().bind(
            Bindings.createDoubleBinding(() -> systemConnectionViewModel.getDestination().getSize().getY(),
                systemConnectionViewModel.getDestination().getPositionProperty()));
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
    }

    private void constructVisualization() {
        setStroke(Color.BLACK);
        setStrokeWidth(20);
    }
}
