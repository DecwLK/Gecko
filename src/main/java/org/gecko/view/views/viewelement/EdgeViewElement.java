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
    private static final double LOOP_RADIUS = 40;

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
        ListChangeListener<? super EdgeViewModel> updateMaskPathSource = change -> maskPathSource();
        edgeViewModel.getSource().getIncomingEdges().addListener(updateMaskPathSource);
        edgeViewModel.getSource().getOutgoingEdges().addListener(updateMaskPathSource);
        edgeViewModel.getDestination().getIncomingEdges().addListener(updateMaskPathSource);
        edgeViewModel.getDestination().getOutgoingEdges().addListener(updateMaskPathSource);

        edgeViewModel.getSourceProperty().addListener((observable, oldValue, newValue) -> {
            updateMaskPathSourceListeners(oldValue);
        });

        edgeViewModel.getDestinationProperty().addListener((observable, oldValue, newValue) -> {
            updateMaskPathSourceListeners(oldValue);
        });

        updateMaskPathSourceListeners(null);
    }

    private void updateMaskPathSourceListeners(StateViewModel oldStateViewModel) {
        // Remove listeners from old state view model
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

    private void maskPathSource() {
        // If source and destination are the same, draw a loop
        if (edgeViewModel.getSource() == edgeViewModel.getDestination() && getEdgePoints().size() == 2) {
            setLoop(true);
            getEdgePoints().getFirst().setValue(edgeViewModel.getSource().getPosition());
            getEdgePoints().getLast()
                .setValue(edgeViewModel.getSource().getPosition().add(new Point2D(0, LOOP_RADIUS)));
            return;
        }

        double sourceEdgeOffset = edgeViewModel.getSource().getEdgeOffset(edgeViewModel);
        getEdgePoints().getFirst()
            .setValue(maskBlock(edgeViewModel.getSource().getPosition(), edgeViewModel.getSource().getSize(),
                edgeViewModel.getDestination().getCenter(), edgeViewModel.getSource().getCenter(), sourceEdgeOffset));

        double destinationEdgeOffset = edgeViewModel.getDestination().getEdgeOffset(edgeViewModel);
        getEdgePoints().getLast()
            .setValue(maskBlock(edgeViewModel.getDestination().getPosition(), edgeViewModel.getDestination().getSize(),
                edgeViewModel.getSource().getCenter(), edgeViewModel.getDestination().getCenter(),
                destinationEdgeOffset));
        setLoop(false);
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
