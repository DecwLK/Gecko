package org.gecko.viewmodel;

import lombok.Getter;
import lombok.Setter;
import org.gecko.model.SystemConnection;

@Getter @Setter
public class SystemConnectionViewModel extends PositionableViewModelElement<SystemConnection> {
    private PortViewModel source;
    private PortViewModel destination;

    SystemConnectionViewModel(SystemConnection target) {
        super(target);
        this.source = new PortViewModel(target.getSource());
        this.destination = new PortViewModel(target.getDestination());
    }

    @Override
    public void updateTarget() {
        // Update source:
        if (this.source == null || this.source.target == null) {
            // TODO: Throw exception.
            return;
        }

        if (!this.source.target.equals(super.target.getSource())) {
            super.target.setSource(this.source.target);
        }

        // Update destination:
        if (this.destination == null || this.destination.target == null) {
            // TODO: Throw exception.
            return;
        }

        if (!this.destination.target.equals(super.target.getDestination())) {
            super.target.setDestination(this.destination.target);
        }
    }

    @Override
    public Object accept(PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }
}
