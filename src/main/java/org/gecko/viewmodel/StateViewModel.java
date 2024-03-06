package org.gecko.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.exceptions.ModelException;
import org.gecko.model.State;

/**
 * Represents an abstraction of a {@link State} model element. A {@link StateViewModel} is described by a set of
 * {@link ContractViewModel}s and can target either a regular or a start-{@link State}. Contains methods for managing
 * the afferent data and updating the target-{@link State}.
 */
@Setter
@Getter
public class StateViewModel extends BlockViewModelElement<State> {
    private final BooleanProperty isStartStateProperty;
    private final ListProperty<ContractViewModel> contractsProperty;

    private final ObservableList<EdgeViewModel> incomingEdges;
    private final ObservableList<EdgeViewModel> outgoingEdges;

    public StateViewModel(int id, @NonNull State target) {
        super(id, target);
        this.isStartStateProperty = new SimpleBooleanProperty();
        this.contractsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.incomingEdges = FXCollections.observableArrayList();
        this.outgoingEdges = FXCollections.observableArrayList();
        addEdgeListeners();
    }

    public boolean getIsStartState() {
        return isStartStateProperty.getValue();
    }

    public void setStartState(boolean isStartState) {
        isStartStateProperty.setValue(isStartState);
    }

    @Override
    public void updateTarget() throws ModelException {
        super.updateTarget();
        target.getContracts().clear();
        target.addContracts(contractsProperty.stream().map(ContractViewModel::getTarget).collect(Collectors.toSet()));
    }

    public void addContract(@NonNull ContractViewModel contract) {
        contractsProperty.add(contract);
    }

    public void removeContract(@NonNull ContractViewModel contract) {
        contractsProperty.remove(contract);
    }

    public List<ContractViewModel> getContracts() {
        return new ArrayList<>(contractsProperty);
    }

    @Override
    public Object accept(@NonNull PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }

    private void addEdgeListeners() {
        updateEdgeOffset();
        getIncomingEdges().addListener((ListChangeListener<EdgeViewModel>) c -> updateEdgeOffset());
        getOutgoingEdges().addListener((ListChangeListener<EdgeViewModel>) c -> updateEdgeOffset());
        getPositionProperty().addListener((observable, oldValue, newValue) -> updateEdgeOffset());
    }

    private void updateEdgeOffset() {
        notifyOtherState();
        setEdgeOffsets();
    }

    private void notifyOtherState() {
        getOutgoingEdges().forEach(edge -> edge.getDestination().setEdgeOffsets());
        getIncomingEdges().forEach(edge -> edge.getSource().setEdgeOffsets());
    }

    public void setEdgeOffsets() {
        Map<Integer, List<EdgeViewModel>> intersectionOrientationEdges = getIntersectionOrientationEdges();
        for (int orientation : intersectionOrientationEdges.keySet()) {
            int count = intersectionOrientationEdges.get(orientation).size();
            double width = getSize().getX();
            double height = getSize().getY();
            double part = (orientation % 2 == 0 ? width : height) / (count + 1);
            double offset = part;
            for (EdgeViewModel edge : intersectionOrientationEdges.get(orientation)) {
                switch (orientation) {
                    case 0:
                        setOffset(edge, -width / 2 + offset, -height / 2);
                        break;
                    case 1:
                        setOffset(edge, width / 2, -height / 2 + offset);
                        break;
                    case 2:
                        setOffset(edge, width / 2 - offset, height / 2);
                        break;
                    case 3:
                        setOffset(edge, -width / 2, height / 2 - offset);
                        break;
                    default:
                        continue;
                }
                offset += part;
            }
        }
    }

    private void setOffset(EdgeViewModel edge, double x, double y) {
        if (edge.getSource().equals(this)) {
            edge.setStartOffsetProperty(new Point2D(x, y));
        } else {
            edge.setEndOffsetProperty(new Point2D(x, y));
        }
    }

    private Map<Integer, List<EdgeViewModel>> getIntersectionOrientationEdges() {
        Map<Integer, List<EdgeViewModel>> intersectionOrientationEdges = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            intersectionOrientationEdges.put(i, new ArrayList<>());
        }
        List<EdgeViewModel> edges = new ArrayList<>(getIncomingEdges());
        edges.addAll(getOutgoingEdges().reversed());
        for (EdgeViewModel edge : edges) {
            Point2D p1 = edge.getSource().getCenter();
            Point2D p2 = edge.getDestination().getCenter();
            int orientation = getIntersectionOrientation(p1, p2);
            if (orientation != -1) {
                intersectionOrientationEdges.get(orientation).add(edge);
            }
        }
        return intersectionOrientationEdges;
    }

    private int getIntersectionOrientation(Point2D p1, Point2D p2) {
        List<Point2D> edgePoints = new ArrayList<>(
            List.of(getPosition(), getPosition().add(getSize().getX(), 0), getPosition().add(getSize()),
                getPosition().add(0, getSize().getY())));
        for (int i = 0; i < edgePoints.size(); i++) {
            Point2D p3 = edgePoints.get(i);
            Point2D p4 = edgePoints.get((i + 1) % 4);
            if (lineIntersectsLine(p1, p2, p3, p4)) {
                System.out.println("State " + getName() + ":" + i);
                return i;
            }
        }
        return -1;
    }

    private static boolean lineIntersectsLine(Point2D l1p1, Point2D l1p2, Point2D l2p1, Point2D l2p2) {
        double s1X = l1p2.getX() - l1p1.getX();
        double s1Y = l1p2.getY() - l1p1.getY();
        double s2X = l2p2.getX() - l2p1.getX();
        double s2Y = l2p2.getY() - l2p1.getY();

        double v = -s2X * s1Y + s1X * s2Y;
        double s = (-s1Y * (l1p1.getX() - l2p1.getX()) + s1X * (l1p1.getY() - l2p1.getY())) / v;
        double t = (s2X * (l1p1.getY() - l2p1.getY()) - s2Y * (l1p1.getX() - l2p1.getX())) / v;

        return s >= 0 && s <= 1 && t >= 0 && t <= 1;
    }
}
