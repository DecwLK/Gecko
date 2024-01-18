package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.model.SystemConnection;

/**
 * Represents an abstraction of a {@link SystemConnection} model element.
 * A {@link SystemConnectionViewModel} is described by a source- and a destination-{@link PortViewModel}.
 * Contains methods for managing the afferent data and updating the target-{@link SystemConnection}.
 */
@Getter
@Setter
public class SystemConnectionViewModel extends PositionableViewModelElement<SystemConnection> {
    private final Property<PortViewModel> sourceProperty;
    private final Property<PortViewModel> destinationProperty;

    SystemConnectionViewModel(int id, SystemConnection target, @NonNull PortViewModel source, @NonNull PortViewModel destination) {
        super(id, target);
        this.sourceProperty = new SimpleObjectProperty<>(source);
        this.destinationProperty = new SimpleObjectProperty<>(destination);
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
    public void updateTarget() {
        target.setSource(getSource().getTarget());
        target.setDestination(getDestination().getTarget());
    }

    @Override
    public Object accept(@NonNull PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }
}
