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
        // TODO
    }

    @Override
    public void accept(PositionableViewModelElementVisitor visitor) {
        visitor.visit(this);
    }
}
