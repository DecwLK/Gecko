package org.gecko.view.views.viewelement;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
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
    private final IntegerProperty priorityProperty;
    private final Property<Kind> kindProperty;

    public EdgeViewElement(EdgeViewModel edgeViewModel) {
        super(edgeViewModel.getEdgePoints());
        this.contractProperty = new SimpleObjectProperty<>();
        this.priorityProperty = new SimpleIntegerProperty();
        this.kindProperty = new SimpleObjectProperty<>();
        this.edgeViewModel = edgeViewModel;
        constructVisualization();

        // Redraw edge when there are changes in the edge list
        edgeViewModel.getSource()
            .getIncomingEdges()
            .addListener((ListChangeListener<? super EdgeViewModel>) c -> maskPathSource());

        edgeViewModel.getSource()
            .getOutgoingEdges()
            .addListener((ListChangeListener<? super EdgeViewModel>) c -> maskPathSource());

        edgeViewModel.getDestination()
            .getIncomingEdges()
            .addListener((ListChangeListener<? super EdgeViewModel>) c -> maskPathSource());

        edgeViewModel.getDestination()
            .getOutgoingEdges()
            .addListener((ListChangeListener<? super EdgeViewModel>) c -> maskPathSource());

        edgeViewModel.getSourceProperty().addListener((observable, oldValue, newValue) -> {
            updateMaskPathSourceListeners(oldValue);
        });

        edgeViewModel.getDestinationProperty().addListener((observable, oldValue, newValue) -> {
            updateMaskPathSourceListeners(oldValue);
        });

        updateMaskPathSourceListeners(null);
    }

    private void updateMaskPathSourceListeners(StateViewModel oldStateViewModel) {
        // Remove listeners
        if (oldStateViewModel != null) {
            oldStateViewModel.getPositionProperty()
                .removeListener((observable, oldValue, newValue) -> maskPathSource());
            oldStateViewModel.getSizeProperty().removeListener((observable, oldValue, newValue) -> maskPathSource());
        }

        maskPathSource();

        edgeViewModel.getSource()
            .getPositionProperty()
            .addListener((observable, oldValue, newValue) -> maskPathSource());

        edgeViewModel.getDestination()
            .getPositionProperty()
            .addListener((observable, oldValue, newValue) -> maskPathSource());
    }

    protected void maskPathSource() {
        double sourceEdgeOffset = edgeViewModel.getSource().getEdgeOffset(edgeViewModel);
        getPathSource().getFirst()
            .setValue(maskBlock(edgeViewModel.getSource().getPosition(), edgeViewModel.getSource().getSize(),
                edgeViewModel.getDestination().getCenter(), edgeViewModel.getSource().getCenter(), sourceEdgeOffset));

        double destinationEdgeOffset = edgeViewModel.getDestination().getEdgeOffset(edgeViewModel);
        getPathSource().getLast()
            .setValue(maskBlock(edgeViewModel.getDestination().getPosition(), edgeViewModel.getDestination().getSize(),
                edgeViewModel.getSource().getCenter(), edgeViewModel.getDestination().getCenter(),
                destinationEdgeOffset));
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
