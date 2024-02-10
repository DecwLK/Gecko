package org.gecko.view.views.viewelement;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
    private static final double FIRST_LOOP_RADIUS = 120;

    @Getter(AccessLevel.NONE)
    private final EdgeViewModel edgeViewModel;
    private final Property<ContractViewModel> contractProperty;
    private final IntegerProperty priorityProperty;
    private final Property<Kind> kindProperty;
    private final Group pane;
    private final Label label;

    private ListChangeListener<? super EdgeViewModel> sourceMaskPathListener;
    private ListChangeListener<? super EdgeViewModel> destinationMaskPathListener;

    public EdgeViewElement(EdgeViewModel edgeViewModel) {
        super(edgeViewModel.getEdgePoints());
        this.contractProperty = new SimpleObjectProperty<>();
        this.priorityProperty = new SimpleIntegerProperty();
        this.kindProperty = new SimpleObjectProperty<>();
        this.edgeViewModel = edgeViewModel;
        this.pane = new Group();
        pane.getChildren().add(this);
        pane.setManaged(false);

        label = new Label();
        label.textProperty()
            .bind(Bindings.createStringBinding(edgeViewModel::getRepresentation, edgeViewModel.getContractProperty(),
                edgeViewModel.getKindProperty(), edgeViewModel.getPriorityProperty()));

        edgeViewModel.getEdgePoints().getFirst().addListener((observable, oldValue, newValue) -> {
            calculateLabelPosition();
            maskPathSource();
        });
        edgeViewModel.getEdgePoints().getLast().addListener((observable, oldValue, newValue) -> {
            calculateLabelPosition();
            maskPathSource();
        });
        label.heightProperty().addListener((observable, oldValue, newValue) -> calculateLabelPosition());
        label.widthProperty().addListener((observable, oldValue, newValue) -> calculateLabelPosition());
        pane.getChildren().add(label);

        constructVisualization();

        // Redraw edge when there are changes in the edge list
        edgeViewModel.getSourceProperty().addListener((observable, oldValue, newValue) -> {
            oldValue.getIncomingEdges().removeListener(sourceMaskPathListener);
            oldValue.getOutgoingEdges().removeListener(sourceMaskPathListener);
            sourceMaskPathListener = updateMaskPathSourceListeners(newValue);
        });

        edgeViewModel.getDestinationProperty().addListener((observable, oldValue, newValue) -> {
            oldValue.getIncomingEdges().removeListener(destinationMaskPathListener);
            oldValue.getOutgoingEdges().removeListener(destinationMaskPathListener);
            destinationMaskPathListener = updateMaskPathSourceListeners(newValue);
        });

        sourceMaskPathListener = updateMaskPathSourceListeners(edgeViewModel.getSource());
        destinationMaskPathListener = updateMaskPathSourceListeners(edgeViewModel.getDestination());

        calculateLabelPosition();
    }

    private void calculateLabelPosition() {
        Point2D first;
        Point2D last;
        if (edgeViewModel.getSource().equals(edgeViewModel.getDestination())) {
            first =
                new Point2D(renderPathSource.get(2).getKey().getValue(), renderPathSource.get(2).getValue().getValue());
            last =
                new Point2D(renderPathSource.get(3).getKey().getValue(), renderPathSource.get(3).getValue().getValue());
        } else {
            first = edgeViewModel.getEdgePoints().getFirst().getValue();
            last = edgeViewModel.getEdgePoints().getLast().getValue();
        }

        Point2D mid = new Point2D((first.getX() + last.getX()) / 2, (first.getY() + last.getY()) / 2);
        double angle = Math.atan2((last.getY() - first.getY()), (last.getX() - first.getX()));
        double mp = 0;
        boolean h = false;
        if (Math.abs(Math.abs(angle) - Math.PI / 2) < Math.PI / 4) {
            h = true;
            mp = (label.getHeight() / 2) / Math.sin(angle);
        } else {
            mp = (label.getWidth() / 2) / Math.cos(angle);
        }

        Point2D vec = last.subtract(first);
        Point2D p;
        Point2D newPos;
        if (!h) {
            if (angle > 0 && angle < Math.PI / 2 || angle < -(1.0 / 2.0) * Math.PI && angle > -Math.PI) {
                p = vec.normalize().multiply(-mp).add(mid);
                newPos = p.subtract(0, label.getHeight());
            } else {
                p = vec.normalize().multiply(mp).add(mid);
                newPos = p.subtract(label.getWidth(), label.getHeight());
            }
        } else {
            if (angle > 0 && angle < Math.PI / 2 || angle < -(1.0 / 2.0) * Math.PI && angle > -Math.PI) {
                p = vec.normalize().multiply(Math.abs(mp)).multiply(Math.signum(angle)).add(mid);
                newPos = p.subtract(0, label.getHeight());
            } else {
                p = vec.normalize().multiply(Math.abs(mp)).multiply(Math.signum(angle)).add(mid);
                newPos = p.subtract(label.getWidth(), label.getHeight());
            }
        }

        label.setLayoutX(newPos.getX());
        label.setLayoutY(newPos.getY());

    }

    private ListChangeListener<? super EdgeViewModel> updateMaskPathSourceListeners(StateViewModel newStateViewModel) {
        ListChangeListener<? super EdgeViewModel> updateMaskPathSource = change -> maskPathSource();
        newStateViewModel.getIncomingEdges().addListener(updateMaskPathSource);
        newStateViewModel.getOutgoingEdges().addListener(updateMaskPathSource);
        maskPathSource();
        return updateMaskPathSource;
    }

    private void maskPathSource() {
        // If source and destination are the same, draw a loop
        if (edgeViewModel.getSource() == edgeViewModel.getDestination() && getEdgePoints().size() == 2) {
            setLoop(true);
            setEdgePoint(0, edgeViewModel.getSource().getPosition());
            setEdgePoint(getEdgePoints().size() - 1, edgeViewModel.getSource()
                .getPosition()
                .add(new Point2D(0,
                    edgeViewModel.getSource().getLoopOffset(edgeViewModel) * LOOP_RADIUS + FIRST_LOOP_RADIUS)));
            updatePathVisualization();
            return;
        }

        double sourceEdgeOffset = edgeViewModel.getSource().getEdgeOffset(edgeViewModel);
        setEdgePoint(0, maskBlock(edgeViewModel.getSource().getPosition(), edgeViewModel.getSource().getSize(),
            edgeViewModel.getDestination().getCenter(), edgeViewModel.getSource().getCenter(), sourceEdgeOffset));

        double destinationEdgeOffset = edgeViewModel.getDestination().getEdgeOffset(edgeViewModel);
        setEdgePoint(getEdgePoints().size() - 1,
            maskBlock(edgeViewModel.getDestination().getPosition(), edgeViewModel.getDestination().getSize(),
                edgeViewModel.getSource().getCenter(), edgeViewModel.getDestination().getCenter(),
                destinationEdgeOffset));
        setLoop(false);
    }

    @Override
    public Node drawElement() {
        return pane;
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
