package org.gecko.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Condition;
import org.gecko.model.Contract;

@Getter
@Setter
public class ContractViewModel extends AbstractViewModelElement<Contract> implements Renamable {

    private final StringProperty nameProperty;
    private final StringProperty preConditionProperty;
    private final StringProperty postConditionProperty;

    public ContractViewModel(Contract target) {
        super(target);
        this.nameProperty = new SimpleStringProperty(target.getName());
        this.preConditionProperty = new SimpleStringProperty(target.getPreCondition().getCondition());
        this.postConditionProperty = new SimpleStringProperty(target.getPreCondition().getCondition());
    }

    @Override
    public void updateTarget() {
        // Update name:
        if (this.nameProperty == null || this.nameProperty.getValue() == null || this.nameProperty.getValue().isEmpty()) {
            // TODO: Throw exception.
            return;
        }

        if (!this.nameProperty.getValue().equals(super.target.getName())) {
            super.target.setName(this.nameProperty.getValue());
        }

        // Update precondition:
        if (this.preConditionProperty == null || this.preConditionProperty.getValue() == null || this.preConditionProperty.getValue().isEmpty()) {
            // TODO: Throw exception.
            return;
        }

        if (!this.preConditionProperty.getValue().equals(super.target.getPreCondition().getCondition())) {
            super.target.setPreCondition(new Condition(this.preConditionProperty.getValue()));
        }

        // Update postcondition:
        if (this.postConditionProperty == null || this.postConditionProperty.getValue() == null || this.postConditionProperty.getValue().isEmpty()) {
            // TODO: Throw exception.
            return;
        }

        if (!this.postConditionProperty.getValue().equals(super.target.getPostCondition().getCondition())) {
            super.target.setPostCondition(new Condition(this.postConditionProperty.getValue()));
        }
    }

    @Override
    public String getName() {
        return this.nameProperty.getValue();
    }

    @Override
    public void setName(String name) {
        this.nameProperty.setValue(name);
    }
}