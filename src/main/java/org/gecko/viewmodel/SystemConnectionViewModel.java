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
import org.gecko.model.Visibility;

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
    }

    public void setSource(@NonNull PortViewModel source) {
        sourceProperty.setValue(source);
    }

    public PortViewModel getSource() {
        return sourceProperty.getValue();
    }

    public void setDestination(@NonNull PortViewModel destination) {
        destinationProperty.setValue(destination);
    }

    public PortViewModel getDestination() {
        return destinationProperty.getValue();
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
    public void setEdgePoint(int index, Point2D newPosition) {
        edgePoints.get(index).setValue(newPosition);
    }

    /**
     * Checks if a connection between the given source and destination port is allowed.
     *
     * @param source            the new source port
     * @param destination       the new destination port
     * @param sourceSystem      the system that contains the source port
     * @param destinationSystem the system that contains the destination port
     * @param parentSystem      the system where the connection is created
     * @param systemConnection  the connection that is being edited, or null if a new connection is being created
     * @return true if the connection is allowed, false otherwise
     */
    public static boolean isConnectingAllowed(
        @NonNull PortViewModel source, @NonNull PortViewModel destination, @NonNull SystemViewModel sourceSystem,
        @NonNull SystemViewModel destinationSystem, @NonNull SystemViewModel parentSystem,
        SystemConnectionViewModel systemConnection) {
        if (destination.getTarget().isHasIncomingConnection() && !(systemConnection != null
            && systemConnection.getDestination().equals(destination))) {
            return false;
        }
        if (sourceSystem.equals(destinationSystem)) {
            return false;
        }

        if (!sourceSystem.equals(parentSystem) && !destinationSystem.equals(parentSystem)) {
            return source.getVisibility() == Visibility.OUTPUT && destination.getVisibility() == Visibility.INPUT;
        } else if (sourceSystem.equals(parentSystem)) {
            return source.getVisibility() != Visibility.OUTPUT && destination.getVisibility() != Visibility.OUTPUT;
        } else {
            return source.getVisibility() != Visibility.INPUT && destination.getVisibility() != Visibility.INPUT;
        }
    }

    @Override
    public void setPosition(@NonNull Point2D position) {
        return;
    }

    @Override
    public void setSize(@NonNull Point2D position) {
        return;
    }
}