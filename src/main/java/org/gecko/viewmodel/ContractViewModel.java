package org.gecko.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Condition;
import org.gecko.model.Contract;

public class ContractViewModel extends AbstractViewModelElement<Contract> implements Renamable {
    private StringProperty name;

    @Getter @Setter private StringProperty preCondition;

    @Getter @Setter private StringProperty postCondition;

    public ContractViewModel(Contract target) {
        super(target);
        this.name = new SimpleStringProperty(target.getName());
        this.preCondition = new SimpleStringProperty(target.getPreCondition().getCondition());
        this.postCondition = new SimpleStringProperty(target.getPreCondition().getCondition());
    }

    @Override
    public void updateTarget() {
        // Update name:
        if (this.name == null || this.name.getValue() == null || this.name.getValue().isEmpty()) {
            // TODO: Throw exception.
            return;
        }

        if (!this.name.getValue().equals(super.getTarget().getName())) {
            super.getTarget().setName(this.name.getValue());
        }

        // Update precondition:
        if (this.preCondition == null || this.preCondition.getValue() == null || this.preCondition.getValue().isEmpty()) {
            // TODO: Throw exception.
            return;
        }

        if (!this.preCondition.getValue().equals(super.getTarget().getPreCondition().getCondition())) {
            super.getTarget().setPreCondition(new Condition(this.preCondition.getValue()));
        }

        // Update postcondition:
        if (this.postCondition == null || this.postCondition.getValue() == null || this.postCondition.getValue().isEmpty()) {
            // TODO: Throw exception.
            return;
        }

        if (!this.postCondition.getValue().equals(super.getTarget().getPostCondition().getCondition())) {
            super.getTarget().setPostCondition(new Condition(this.postCondition.getValue()));
        }
    }

    @Override
    public String getName() {
        return this.name.getValue();
    }

    @Override
    public void setName(String name) {
        // TODO: further checks before updating?
        this.name.setValue(name);
    }
}