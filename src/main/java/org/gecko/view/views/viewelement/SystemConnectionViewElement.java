package org.gecko.view.views.viewelement;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import lombok.AccessLevel;
import lombok.Getter;
import org.gecko.model.Visibility;
import org.gecko.viewmodel.SystemConnectionViewModel;

/**
 * Represents a type of {@link ConnectionViewElement} implementing the {@link ViewElement} interface, which encapsulates
 * an {@link SystemConnectionViewModel}.
 */
@Getter
public class SystemConnectionViewElement extends ConnectionViewElement
    implements ViewElement<SystemConnectionViewModel> {

    private static final int Z_PRIORITY = 20;

    @Getter(AccessLevel.NONE)
    private final SystemConnectionViewModel systemConnectionViewModel;
    private final Property<Visibility> visibilityProperty;
    private final StringProperty typeProperty;

    public SystemConnectionViewElement(SystemConnectionViewModel systemConnectionViewModel) {
        super(systemConnectionViewModel.getEdgePoints());
        this.visibilityProperty = new SimpleObjectProperty<>();
        this.typeProperty = new SimpleStringProperty();
        this.systemConnectionViewModel = systemConnectionViewModel;

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
        systemConnectionViewModel.setEdgePoint(index, point);
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

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
    }

    private void constructVisualization() {
        setStroke(Color.BLACK);
        setSmooth(true);
    }
}
