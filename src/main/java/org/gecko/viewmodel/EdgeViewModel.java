package org.gecko.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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
public class EdgeViewModel extends PositionableViewModelElement<Edge> {

    private final Property<Kind> kindProperty;
    private final IntegerProperty priorityProperty;
    private final Property<ContractViewModel> contractProperty;
    private final Property<StateViewModel> sourceProperty;
    private final Property<StateViewModel> destinationProperty;
    private final BooleanProperty isLoopProperty;
    private final IntegerProperty orientationProperty;
    /**
     * The list of edge points that define the path of the edge.
     */

    private final Property<Point2D> startPointProperty;
    private final Property<Point2D> endPointProperty;
    private final Property<Point2D> startOffsetProperty;
    private final Property<Point2D> endOffsetProperty;

    public EdgeViewModel(
        int id, @NonNull Edge target, @NonNull StateViewModel source, @NonNull StateViewModel destination) {
        super(id, target);
        this.kindProperty = new SimpleObjectProperty<>(target.getKind());
        this.priorityProperty = new SimpleIntegerProperty(target.getPriority());
        this.contractProperty = new SimpleObjectProperty<>();
        this.sourceProperty = new SimpleObjectProperty<>(source);
        this.destinationProperty = new SimpleObjectProperty<>(destination);
        this.isLoopProperty = new SimpleBooleanProperty();
        isLoopProperty.bind(Bindings.createBooleanBinding(() -> getSource().equals(getDestination()), sourceProperty,
            destinationProperty));
        this.orientationProperty = new SimpleIntegerProperty();

        this.startOffsetProperty = new SimpleObjectProperty<>(Point2D.ZERO);
        this.endOffsetProperty = new SimpleObjectProperty<>(Point2D.ZERO);
        this.startPointProperty = new SimpleObjectProperty<>();
        this.endPointProperty = new SimpleObjectProperty<>();
        sizeProperty.setValue(Point2D.ZERO);
        setBindings();

        getSource().getOutgoingEdges().add(this);
        getDestination().getIncomingEdges().add(this);
    }

    public void setBindings() {
        startPointProperty.bind(Bindings.createObjectBinding(() -> {
            return getSource().getCenter().add(startOffsetProperty.getValue());
        }, startOffsetProperty, getSource().getPositionProperty()));
        endPointProperty.bind(Bindings.createObjectBinding(() -> {
            return getDestination().getCenter().add(endOffsetProperty.getValue());
        }, endOffsetProperty, getDestination().getPositionProperty()));
    }

    private void removeBindings() {
        startPointProperty.unbind();
        endPointProperty.unbind();
    }

    public void setStartOffsetProperty(Point2D startOffset) {
        this.startOffsetProperty.setValue(startOffset);
    }

    public void setEndOffsetProperty(Point2D endOffset) {
        this.endOffsetProperty.setValue(endOffset);
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
        getSource().getOutgoingEdges().remove(this);
        removeBindings();
        sourceProperty.setValue(source);
        setBindings();
        source.getOutgoingEdges().add(this);
    }

    public StateViewModel getSource() {
        return sourceProperty.getValue();
    }

    public void setDestination(@NonNull StateViewModel destination) {
        getDestination().getIncomingEdges().remove(this);
        removeBindings();
        destinationProperty.setValue(destination);
        setBindings();
        destination.getIncomingEdges().add(this);
    }

    public StateViewModel getDestination() {
        return destinationProperty.getValue();
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
    public <S> S accept(@NonNull PositionableViewModelElementVisitor<S> visitor) {
        return visitor.visit(this);
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

    public Point2D getStartPoint() {
        return startPointProperty.getValue();
    }

    public Point2D getEndPoint() {
        return endPointProperty.getValue();
    }

    public void setStartPoint(Point2D startPoint) {
        removeBindings();
        startPointProperty.setValue(startPoint);
    }

    public void setEndPoint(Point2D endPoint) {
        removeBindings();
        endPointProperty.setValue(endPoint);
    }

    public boolean isLoop() {
        return isLoopProperty.getValue();
    }

    public void setOrientation(int orientation) {
        orientationProperty.setValue(orientation);
    }

    @Override
    public void setPosition(@NonNull Point2D position) {
    }

    @Override
    public void setSize(@NonNull Point2D position) {
    }

    @Override
    public Point2D getCenter() {
        return startPointProperty.getValue().midpoint(endPointProperty.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EdgeViewModel edge)) {
            return false;
        }
        return id == edge.id;
    }
}
