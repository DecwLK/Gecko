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
    private StringProperty codeProperty;
    private ObservableList<PortViewModel> portsProperty; //TODO should this be called property?

    public SystemViewModel(System target) {
        super(target);
        super.setName(target.getName());
        this.codeProperty = new SimpleStringProperty(target.getCode());
        this.portsProperty = FXCollections.observableArrayList();
    }

    public String getCode() {
        return codeProperty.getValue();
    }

    public void setCode(String code) {
        codeProperty.setValue(code);
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
        if (this.codeProperty == null || this.codeProperty.getValue() == null || this.codeProperty.getValue().isEmpty()) {
            // TODO: Throw exception.
            return;
        }

        if (!this.codeProperty.getValue().equals(super.target.getCode())) {
            super.target.setCode(this.codeProperty.getValue());
        }
    }

    public void addPort(PortViewModel port) {
        // TODO: prior checks
        this.portsProperty.add(port);
        super.target.addVariable(port.target);
    }

    @Override
    public Object accept(PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }
}
