package org.gecko.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.System;

@Getter
@Setter
public class SystemViewModel extends BlockViewModelElement<System> {
    private StringProperty code;
    private ObservableList<PortViewModel> ports;

    public SystemViewModel(System target) {
        super(target);
        super.setName(target.getName());
        this.code = new SimpleStringProperty(target.getCode());
        this.ports = FXCollections.observableArrayList();
    }

    @Override
    public void updateTarget() {
        // Update name:
        if (super.getName() == null || super.getName().isEmpty()) {
            // TODO: Throw exception.
            return;
        }

        if (!super.getName().equals(super.target.getName())) {
            super.target.setName(super.getName());
        }

        // Update code:
        // TODO: can be empty?
        if (this.code == null || this.code.getValue() == null || this.code.getValue().isEmpty()) {
            // TODO: Throw exception.
            return;
        }

        if (!this.code.getValue().equals(super.target.getCode())) {
            super.target.setCode(this.code.getValue());
        }
    }

    public void addPort(PortViewModel port) {
        // TODO: prior checks
        this.ports.add(port);
        super.target.addVariable(port.target);
    }

    @Override
    public Object accept(PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }
}
