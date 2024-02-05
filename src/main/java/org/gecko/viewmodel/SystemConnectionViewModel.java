package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.exceptions.ModelException;
import org.gecko.model.SystemConnection;

/**
 * Represents an abstraction of a {@link SystemConnection} model element. A {@link SystemConnectionViewModel} is
 * described by a source- and a destination-{@link PortViewModel}. Contains methods for managing the afferent data and
 * updating the target-{@link SystemConnection}.
 */
@Getter
@Setter
public class SystemConnectionViewModel extends PositionableViewModelElement<SystemConnection>
    implements ConnectionViewModel {
    private final Property<PortViewModel> sourceProperty;
    private final Property<PortViewModel> destinationProperty;
    private final ObservableList<Property<Point2D>> edgePoints;

    SystemConnectionViewModel(
        int id, SystemConnection target, @NonNull PortViewModel source, @NonNull PortViewModel destination) {
        super(id, target);
        this.sourceProperty = new SimpleObjectProperty<>(source);
        this.destinationProperty = new SimpleObjectProperty<>(destination);
        this.edgePoints = FXCollections.observableArrayList();

        updateConnectionListener();
    }

    public void setSource(@NonNull PortViewModel source) {
        clearConnectionListener();
        sourceProperty.setValue(source);
        updateConnectionListener();
        updateTarget();
    }

    public PortViewModel getSource() {
        return sourceProperty.getValue();
    }

    public void setDestination(@NonNull PortViewModel destination) {
        clearConnectionListener();
        destinationProperty.setValue(destination);
        updateConnectionListener();
        updateTarget();
    }

    public PortViewModel getDestination() {
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
        target.setSource(getSource().getTarget());
        target.setDestination(getDestination().getTarget());
    }

    @Override
    public Object accept(@NonNull PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public void setEdgePoint(int index, Point2D point) {
        edgePoints.get(index).setValue(point);
    }
}
