package org.gecko.view.views.viewelement;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import org.gecko.model.Kind;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.EdgeViewModel;

/**
 * Represents a type of {@link ConnectionViewElement} implementing the {@link ViewElement} interface, which encapsulates
 * an {@link EdgeViewModel}.
 */
@Getter
public class EdgeViewElement extends ConnectionViewElement implements ViewElement<EdgeViewModel> {

    private static final int Z_PRIORITY = 20;
    private static final double LABEL_OFFSET = 15.0;

    @Getter(AccessLevel.NONE)
    private final EdgeViewModel edgeViewModel;
    private final Property<ContractViewModel> contractProperty;
    private final IntegerProperty priorityProperty;
    private final Property<Kind> kindProperty;
    private final Group pane;
    private final Label label;

    public EdgeViewElement(EdgeViewModel edgeViewModel) {
        super(FXCollections.observableArrayList(edgeViewModel.getStartPointProperty(),
            edgeViewModel.getEndPointProperty()));
        this.contractProperty = new SimpleObjectProperty<>();
        this.priorityProperty = new SimpleIntegerProperty();
        this.kindProperty = new SimpleObjectProperty<>();
        this.contractProperty.bind(edgeViewModel.getContractProperty());
        this.priorityProperty.bind(edgeViewModel.getPriorityProperty());
        this.kindProperty.bind(edgeViewModel.getKindProperty());
        this.edgeViewModel = edgeViewModel;
        this.pane = new Group();
        pane.getChildren().add(this);
        pane.setManaged(false);

        this.label = new Label();
        label.setText(edgeViewModel.getRepresentation());

        ChangeListener<Object> updateLabelPosition = (observable, oldValue, newValue) -> calculateLabelPosition();
        label.heightProperty().addListener(updateLabelPosition);
        label.widthProperty().addListener(updateLabelPosition);
        edgeViewModel.getStartPointProperty().addListener(updateLabelPosition);
        edgeViewModel.getEndPointProperty().addListener(updateLabelPosition);

        ChangeListener<Object> updateLabel =
            (observable, oldValue, newValue) -> label.setText(edgeViewModel.getRepresentation());

        ContractViewModel contract = edgeViewModel.getContract();
        if (contract != null) {
            contract.getNameProperty().addListener(updateLabel);
        }
        contractProperty.addListener((observable, oldValue, newValue) -> {
            label.setText(edgeViewModel.getRepresentation());
            if (newValue != null) {
                newValue.getNameProperty().addListener(updateLabel);
            }
        });
        priorityProperty.addListener(updateLabel);
        kindProperty.addListener(updateLabel);

        pane.getChildren().add(label);

        isLoopProperty.bind(edgeViewModel.getIsLoopProperty().and(edgeViewModel.getIsCurrentlyModified().not()));
        orientationProperty.bind(edgeViewModel.getOrientationProperty());

        isLoopProperty.addListener((observable, oldValue, newValue) -> calculateLabelPosition());
        constructVisualization();
        calculateLabelPosition();
    }

    private void calculateLabelPosition() {
        Point2D first;
        Point2D last;
        if (isLoopProperty.get()) {
            Pair<Point2D, Point2D> loopPoints = getLoopPoints();
            first = loopPoints.getKey();
            last = loopPoints.getValue();
        } else {
            first = edgeViewModel.getStartPoint();
            last = edgeViewModel.getEndPoint();
        }
        Point2D mid = first.midpoint(last);
        Point2D vec = last.subtract(first);
        double angle = Math.atan2(vec.getY(), vec.getX());
        boolean isVertical = Math.abs(Math.abs(angle) - Math.PI / 2) < Math.PI / 4;
        boolean isPart = (angle > 0 && angle < Math.PI / 2) || (angle < -(1.0 / 2.0) * Math.PI && angle > -Math.PI);

        Point2D p;
        Point2D newPos;
        double mp;
        if (isVertical) {
            mp = (label.getHeight() / 2) / Math.sin(angle);
            p = vec.normalize().multiply(Math.abs(mp)).multiply(Math.signum(angle)).add(mid);
        } else {
            mp = (label.getWidth() / 2) / Math.cos(angle);
            Point2D sized = vec.normalize().multiply(mp);
            sized = isPart ? sized.multiply(-1) : sized;
            p = sized.add(mid);
        }

        newPos = p.subtract(isPart ? 0 : label.getWidth(), label.getHeight());

        label.setLayoutX(newPos.getX());
        label.setLayoutY(newPos.getY());
    }

    private Pair<Point2D, Point2D> getLoopPoints() {
        double minY = Math.min(edgeViewModel.getStartPoint().getY(), edgeViewModel.getEndPoint().getY());
        double maxY = Math.max(edgeViewModel.getStartPoint().getY(), edgeViewModel.getEndPoint().getY());
        double offsetY = maxY - minY;
        double minX = Math.min(edgeViewModel.getStartPoint().getX(), edgeViewModel.getEndPoint().getX());
        double maxX = Math.max(edgeViewModel.getStartPoint().getX(), edgeViewModel.getEndPoint().getX());
        double offsetX = maxX - minX;
        return switch (orientationProperty.get()) {
            case 0 -> new Pair<>(new Point2D(minX, minY - offsetY), new Point2D(maxX + offsetX, minY - offsetY));
            case 1 -> new Pair<>(new Point2D(minX, maxY + offsetY + LABEL_OFFSET),
                new Point2D(maxX + offsetX, maxY + offsetY + LABEL_OFFSET));
            case 2 -> new Pair<>(new Point2D(minX - offsetX, maxY + offsetY + LABEL_OFFSET),
                new Point2D(maxX, maxY + offsetY + LABEL_OFFSET));
            case 3 -> new Pair<>(new Point2D(minX - offsetX, minY - offsetY), new Point2D(maxX, minY - offsetY));
            default -> new Pair<>(Point2D.ZERO, Point2D.ZERO);
        };
    }

    @Override
    public Node drawElement() {
        return pane;
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

    @Override
    public ObservableList<Property<Point2D>> getEdgePoints() {
        return FXCollections.observableArrayList(edgeViewModel.getStartPointProperty(),
            edgeViewModel.getEndPointProperty());
    }

    @Override
    public boolean setEdgePoint(int index, Point2D point) {
        if (index == 0) {
            edgeViewModel.setStartPoint(point);
            return true;
        } else if (index == 1) {
            edgeViewModel.setEndPoint(point);
            return true;
        }
        return false;
    }
}
