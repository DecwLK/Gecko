package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.model.SystemConnection;

@Getter
@Setter
public class SystemConnectionViewModel extends PositionableViewModelElement<SystemConnection> {
    private final Property<PortViewModel> sourceProperty;
    private final Property<PortViewModel> destinationProperty;

    SystemConnectionViewModel(int id, SystemConnection target) {
        super(id, target);
        this.sourceProperty = new SimpleObjectProperty<>();
        this.destinationProperty = new SimpleObjectProperty<>();
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
