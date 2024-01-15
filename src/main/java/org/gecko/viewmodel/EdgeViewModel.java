package org.gecko.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Edge;
import org.gecko.model.Kind;

@Getter
@Setter
public class EdgeViewModel extends PositionableViewModelElement<Edge> {

    private Property<Kind> kindProperty;
    private IntegerProperty priorityProperty;
    private ContractViewModel contract;
    private StateViewModel source;
    private StateViewModel destination;

    public EdgeViewModel(Edge target) {
        super(target);
        this.kindProperty = new SimpleObjectProperty<>(target.getKind());
        this.priorityProperty = new SimpleIntegerProperty(target.getPriority());
        this.contract = null;
        this.source = null;
        this.destination = null;
    }

    @Override
    public void updateTarget() {
        // Update kind:
        if (this.kindProperty == null || this.kindProperty.getValue() == null) {
            // TODO: Throw exception.
            return;
        }

        if (!this.kindProperty.getValue().equals(super.target.getKind())) {
            super.target.setKind(this.kindProperty.getValue());
        }

        // Update priority:
        // TODO: Are there any restrictions regarding what a priority can be? e.g. Are negative
        // numbers allowed?
        if (this.priorityProperty == null || this.priorityProperty.getValue() == null) {
            // TODO: Throw exception.
            return;
        }

        if (!this.priorityProperty.getValue().equals(super.target.getPriority())) {
            super.target.setPriority(this.priorityProperty.getValue());
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
