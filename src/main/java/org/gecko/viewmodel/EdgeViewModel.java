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

        if (!this.kind.getValue().equals(super.target.getKind())) {
            super.target.setKind(this.kind.getValue());
        }

        // Update priority:
        // TODO: Are there any restrictions regarding what a priority can be? e.g. Are negative
        // numbers allowed?
        if (this.priority == null || this.priority.getValue() == null) {
            // TODO: Throw exception.
            return;
        }

        if (!this.priority.getValue().equals(super.target.getPriority())) {
            super.target.setPriority(this.priority.getValue());
        }

        // Update contract:
        if (this.contract == null || this.contract.target == null) {
            // TODO: Throw exception.
            return;
        }

        if (!this.contract.target.equals(super.target.getContract())) {
            super.target.setContract(this.contract.target);
        }

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
