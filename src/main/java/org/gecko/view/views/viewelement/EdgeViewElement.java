package org.gecko.view.views.viewelement;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
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
        super(edgeViewModel.getEdgePoints());
        this.contractProperty = new SimpleObjectProperty<>();
        this.sourceProperty = new SimpleObjectProperty<>();
        this.destinationProperty = new SimpleObjectProperty<>();
        this.priorityProperty = new SimpleIntegerProperty();
        this.kindProperty = new SimpleObjectProperty<>();
        this.edgeViewModel = edgeViewModel;
        constructVisualization();

        edgeViewModel.getSourceProperty().addListener((observable, oldValue, newValue) -> {
            updateMaskPathSourceListeners();
        });

        edgeViewModel.getDestinationProperty().addListener((observable, oldValue, newValue) -> {
            updateMaskPathSourceListeners();
        });

        updateMaskPathSourceListeners();
    }

    public void updateMaskPathSourceListeners() {
        edgeViewModel.getSource()
            .getPositionProperty()
            .addListener((observable, oldValue, newValue) -> maskPathSource());

        edgeViewModel.getDestination()
            .getPositionProperty()
            .addListener((observable, oldValue, newValue) -> maskPathSource());

        maskPathSource();
    }

    private void maskPathSource() {
        getPathSource().getFirst()
            .setValue(maskBlock(edgeViewModel.getSource().getPosition(), edgeViewModel.getSource().getSize(),
                edgeViewModel.getDestination().getCenter(), edgeViewModel.getSource().getCenter()));

                getPathSource().getLast()
                    .setValue(maskBlock(edgeViewModel.getDestination().getPosition(), edgeViewModel.getDestination().getSize(),
                        edgeViewModel.getSource().getCenter(), edgeViewModel.getDestination().getCenter()));
    }

    @Override
    public Node drawElement() {
        return this;
    }

    @Override
    public ObservableList<Property<Point2D>> getEdgePoints() {
        return edgeViewModel.getEdgePoints();
    }

    @Override
    public void setEdgePoint(int index, Point2D point) {
        edgeViewModel.setEdgePoint(index, point);
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
        setSmooth(true);
    }
}
