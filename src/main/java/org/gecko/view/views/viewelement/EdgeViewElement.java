package org.gecko.view.views.viewelement;

import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import lombok.AccessLevel;
import lombok.Getter;
import org.gecko.model.Kind;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.StateViewModel;

@Getter
public class EdgeViewElement extends ConnectionViewElement implements ViewElement<EdgeViewModel> {

    private static final int Z_PRIORITY = 20;

    @Getter(AccessLevel.NONE)
    private final EdgeViewModel edgeViewModel;
    private final Property<ContractViewModel> contractProperty;
    private final Property<StateViewModel> sourceProperty;
    private final Property<StateViewModel> destinationProperty;
    private final IntegerProperty priorityProperty;
    private final Property<Kind> kindProperty;

    public EdgeViewElement(EdgeViewModel edgeViewModel) {
        this.contractProperty = new SimpleObjectProperty<>();
        this.sourceProperty = new SimpleObjectProperty<>();
        this.destinationProperty = new SimpleObjectProperty<>();
        this.priorityProperty = new SimpleIntegerProperty();
        this.kindProperty = new SimpleObjectProperty<>();
        this.edgeViewModel = edgeViewModel;
        bindViewElement();
        constructVisualization();
    }

    private void bindViewElement() {
        getStartElement().xProperty()
                         .bind(Bindings.createDoubleBinding(() -> edgeViewModel.getSource().getCenter().getX(),
                             edgeViewModel.getSource().getPositionProperty()));
        getStartElement().yProperty()
                         .bind(Bindings.createDoubleBinding(() -> edgeViewModel.getSource().getCenter().getY(),
                             edgeViewModel.getSource().getPositionProperty()));
        getEndElement().xProperty()
                       .bind(Bindings.createDoubleBinding(() -> edgeViewModel.getDestination().getCenter().getX(),
                           edgeViewModel.getDestination().getPositionProperty()));
        getEndElement().yProperty()
                       .bind(Bindings.createDoubleBinding(() -> edgeViewModel.getDestination().getCenter().getY(),
                           edgeViewModel.getDestination().getPositionProperty()));
    }

    public void createNewPoint(Point2D point) {
        createPathPoint(point);
    }

    @Override
    public Node drawElement() {
        return this;
    }

    @Override
    public List<Property<Point2D>> getEdgePoints() {
        return getPathPoints();
    }

    @Override
    public void setEdgePoint(int index, Point2D point) {
        setPathPoint(index, point);
    }

    @Override
    public EdgeViewModel getTarget() {
        return edgeViewModel;
    }

    @Override
    public Point2D getPosition() {
        return edgeViewModel.getPosition();
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getZPriority() {
        return Z_PRIORITY;
    }

    private void constructVisualization() {
        setStroke(Color.BLACK);
    }
}
