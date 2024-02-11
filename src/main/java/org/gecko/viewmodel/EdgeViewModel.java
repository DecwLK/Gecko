package org.gecko.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.exceptions.ModelException;
import org.gecko.model.Edge;
import org.gecko.model.Kind;

/**
 * Represents an abstraction of an {@link Edge} model element. An {@link EdgeViewModel} is described by a source- and a
 * destination-{@link StateViewModel}. It is also associated with one of the start-{@link StateViewModel}'s
 * {@link ContractViewModel}s, has a priority and a {@link Kind}, which informs about how the associated
 * {@link ContractViewModel} is handled. Contains methods for managing the afferent data and updating the
 * target-{@link Edge}.
 */
@Getter
@Setter
public class EdgeViewModel extends PositionableViewModelElement<Edge> implements ConnectionViewModel {

    private final Property<Kind> kindProperty;
    private final IntegerProperty priorityProperty;
    private final Property<ContractViewModel> contractProperty;
    private final Property<StateViewModel> sourceProperty;
    private final Property<StateViewModel> destinationProperty;
    /**
     * The list of edge points that define the path of the edge.
     */
    private final ObservableList<Property<Point2D>> edgePoints;

    public EdgeViewModel(
        int id, @NonNull Edge target, @NonNull StateViewModel source, @NonNull StateViewModel destination) {
        super(id, target);
        this.kindProperty = new SimpleObjectProperty<>(target.getKind());
        this.priorityProperty = new SimpleIntegerProperty(target.getPriority());
        this.contractProperty = new SimpleObjectProperty<>();
        this.sourceProperty = new SimpleObjectProperty<>(source);
        this.destinationProperty = new SimpleObjectProperty<>(destination);
        this.edgePoints = FXCollections.observableArrayList();

        Property<Point2D> startPoint = new SimpleObjectProperty<>(getSource().getCenter());
        Property<Point2D> endPoint = new SimpleObjectProperty<>(getDestination().getCenter());

        updateConnectionListener();

        getSource().getOutgoingEdges().add(this);
        getDestination().getIncomingEdges().add(this);

        edgePoints.add(startPoint);
        edgePoints.add(endPoint);
    }

    public void setPriority(int priority) {
        priorityProperty.setValue(priority);
    }

    public int getPriority() {
        return priorityProperty.getValue();
    }

    public void setKind(@NonNull Kind kind) {
        kindProperty.setValue(kind);
    }

    public Kind getKind() {
        return kindProperty.getValue();
    }

    public void setContract(ContractViewModel contract) {
        contractProperty.setValue(contract);
    }

    public ContractViewModel getContract() {
        return contractProperty.getValue();
    }

    public void setSource(@NonNull StateViewModel source) {
        clearConnectionListener();
        getSource().getOutgoingEdges().remove(this);
        sourceProperty.setValue(source);
        getSource().getOutgoingEdges().add(this);
        updateConnectionListener();
    }

    public StateViewModel getSource() {
        return sourceProperty.getValue();
    }

    public void setDestination(@NonNull StateViewModel destination) {
        clearConnectionListener();
        getDestination().getIncomingEdges().remove(this);
        destinationProperty.setValue(destination);
        getDestination().getIncomingEdges().add(this);
        updateConnectionListener();
    }

    public StateViewModel getDestination() {
        return destinationProperty.getValue();
    }

    private void clearConnectionListener() {
        getSource().getPositionProperty()
            .removeListener(
                (observable, oldValue, newValue) -> edgePoints.getFirst().setValue(getSource().getCenter()));

        getDestination().getPositionProperty()
            .removeListener(
                (observable, oldValue, newValue) -> edgePoints.getLast().setValue(getDestination().getCenter()));
    }

    private void updateConnectionListener() {
        getSource().getPositionProperty()
            .addListener((observable, oldValue, newValue) -> edgePoints.getFirst().setValue(getSource().getCenter()));

        getDestination().getPositionProperty()
            .addListener(
                (observable, oldValue, newValue) -> edgePoints.getLast().setValue(getDestination().getCenter()));
    }

    @Override
    public void updateTarget() throws ModelException {
        target.setKind(getKind());
        target.setPriority(getPriority());
        if (contractProperty.getValue() != null) {
            target.setContract(contractProperty.getValue().getTarget());
        }
        target.setSource(sourceProperty.getValue().getTarget());
        target.setDestination(destinationProperty.getValue().getTarget());
    }

    @Override
    public Object accept(@NonNull PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public void setEdgePoint(int index, Point2D newPosition) {
        edgePoints.get(index).setValue(newPosition);
    }

    /**
     * Returns a string representation of this {@link EdgeViewModel} in the form of "priority. kind(contract)".
     *
     * @return a string representation of this {@link EdgeViewModel}
     */
    public String getRepresentation() {
        String representation = "";
        representation += getPriority() + ". ";
        representation += getKind().name();
        if (getContract() != null) {
            representation += "(" + getContract().getName() + ")";
        }
        return representation;
    }
}
