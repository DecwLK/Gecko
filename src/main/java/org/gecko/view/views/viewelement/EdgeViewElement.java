package org.gecko.view.views.viewelement;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Kind;
import org.gecko.view.views.viewelement.decorator.ViewElementDecorator;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.StateViewModel;

@Getter
public class EdgeViewElement extends ConnectionViewElement implements ViewElement<EdgeViewModel> {

    private final EdgeViewModel edgeViewModel;
    private final Property<ContractViewModel> contractProperty;
    private final Property<StateViewModel> sourceProperty;
    private final Property<StateViewModel> destinationProperty;
    private final IntegerProperty priorityProperty;
    private final Property<Kind> kindProperty;

    @Setter
    private ViewElementDecorator decorator;

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
        startXProperty().bind(Bindings.createDoubleBinding(() -> edgeViewModel.getSource().getCenter().getX(),
            edgeViewModel.getSource().getPositionProperty()));
        startYProperty().bind(Bindings.createDoubleBinding(() -> edgeViewModel.getSource().getCenter().getY(),
            edgeViewModel.getSource().getPositionProperty()));
        endXProperty().bind(Bindings.createDoubleBinding(() -> edgeViewModel.getDestination().getCenter().getX(),
            edgeViewModel.getDestination().getPositionProperty()));
        endYProperty().bind(Bindings.createDoubleBinding(() -> edgeViewModel.getDestination().getCenter().getY(),
            edgeViewModel.getDestination().getPositionProperty()));
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
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
    }

    private void constructVisualization() {
        setStroke(Color.BLACK);
    }
}
