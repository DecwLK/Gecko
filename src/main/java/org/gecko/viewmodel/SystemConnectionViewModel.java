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
        if (this.source == null || this.source.getTarget() == null) {
            // TODO: Throw exception.
            return;
        }

        if (!this.source.getTarget().equals(super.getTarget().getSource())) {
            super.getTarget().setSource(this.source.getTarget());
        }

        // Update destination:
        if (this.destination == null || this.destination.getTarget() == null) {
            // TODO: Throw exception.
            return;
        }

        if (!this.destination.getTarget().equals(super.getTarget().getDestination())) {
            super.getTarget().setDestination(this.destination.getTarget());
        }
    }

    @Override
    public void accept(PositionableViewModelElementVisitor visitor) {
        visitor.visit(this);
    }
}
