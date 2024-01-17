package org.gecko.view.views.viewelement;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import lombok.Getter;
import org.gecko.model.Kind;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.StateViewModel;

@Getter
public class EdgeViewElement extends ConnectionViewElement implements ViewElement<EdgeViewModel> {

    private EdgeViewModel edgeViewModel;
    private final Property<ContractViewModel> contractProperty;
    private final Property<StateViewModel> sourceProperty;
    private final Property<StateViewModel> destinationProperty;
    private final IntegerProperty priorityProperty;
    private final Property<Kind> kindProperty;

    public EdgeViewElement() {
        this.contractProperty = new SimpleObjectProperty<>();
        this.sourceProperty = new SimpleObjectProperty<>();
        this.destinationProperty = new SimpleObjectProperty<>();
        this.priorityProperty = new SimpleIntegerProperty();
        this.kindProperty = new SimpleObjectProperty<>();
    }

    @Override
    public Node drawElement() {
        return this;
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
    public void bindTo(EdgeViewModel target) {
        this.edgeViewModel = target;
        startXProperty().bind(
            Bindings.createDoubleBinding(() -> this.edgeViewModel.getSource().getPosition().getX(), this.edgeViewModel.getSource().getPositionProperty()));
        startYProperty().bind(
            Bindings.createDoubleBinding(() -> this.edgeViewModel.getSource().getPosition().getY(), this.edgeViewModel.getSource().getPositionProperty()));
        endXProperty().bind(
            Bindings.createDoubleBinding(() -> this.edgeViewModel.getDestination().getSize().getX(), this.edgeViewModel.getDestination().getSizeProperty()));
        endYProperty().bind(
            Bindings.createDoubleBinding(() -> this.edgeViewModel.getDestination().getSize().getY(), this.edgeViewModel.getDestination().getSizeProperty()));
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
    }
}
