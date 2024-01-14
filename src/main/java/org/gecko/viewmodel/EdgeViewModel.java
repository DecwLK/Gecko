package org.gecko.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Edge;
import org.gecko.model.Kind;

@Setter
@Getter
public class EdgeViewModel extends PositionableViewModelElement<Edge> {
    private Property<Kind> kind;

    private IntegerProperty priority;

    private ContractViewModel contract;

    private StateViewModel source;

    private StateViewModel destination;

    public EdgeViewModel(Edge target) {
        super(target);
        this.kind = new SimpleObjectProperty<>(target.getKind());
        this.priority = new SimpleIntegerProperty(target.getPriority());
        this.contract = new ContractViewModel(target.getContract());
        this.source = new StateViewModel(target.getSource());
        this.destination = new StateViewModel(target.getDestination());
    }

    @Override
    public void updateTarget() {
        // Update kind:
        if (this.kind == null || this.kind.getValue() == null) {
            // TODO: Throw exception.
            return;
        }

        if (!this.kind.getValue().equals(super.getTarget().getKind())) {
            super.getTarget().setKind(this.kind.getValue());
        }

        // Update priority:
        // TODO: Are there any restrictions regarding what a priority can be? e.g. Are negative
        // numbers allowed?
        if (this.priority == null || this.priority.getValue() == null) {
            // TODO: Throw exception.
            return;
        }

        if (!this.priority.getValue().equals(super.getTarget().getPriority())) {
            super.getTarget().setPriority(this.priority.getValue());
        }

        // Update contract:
        if (this.contract == null || this.contract.getTarget() == null) {
            // TODO: Throw exception.
            return;
        }

        if (!this.contract.getTarget().equals(super.getTarget().getContract())) {
            super.getTarget().setContract(this.contract.getTarget());
        }

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
    public Object accept(PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }
}
