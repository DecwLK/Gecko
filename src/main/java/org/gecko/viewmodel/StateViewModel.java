package org.gecko.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.State;

@Setter
@Getter
public class StateViewModel extends BlockViewModelElement<State> {
    private BooleanProperty isStartStateProperty;
    private final ObservableList<ContractViewModel> contractsProperty;

    public StateViewModel(State target) {
        super(target);
        super.setName(target.getName());
        isStartStateProperty = new SimpleBooleanProperty(false);
        this.contractsProperty = FXCollections.observableArrayList();
    }

    public boolean getIsStartState() {
        return isStartStateProperty.getValue();
    }

    public void setStartState(boolean isStartState) {
        isStartStateProperty.setValue(isStartState);
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

        // Update isStartState:
        // TODO: Start state change handled beforehand. Has to be taken care of in automaton also.
        if (this.isStartStateProperty == null || this.isStartStateProperty.getValue() == null) {
            // TODO: Throw exception.
            return;
        }
    }

    public void addContract(ContractViewModel contract) {
        // TODO: prior checks
        this.contractsProperty.add(contract);
        super.target.addContract(contract.target);
    }

    @Override
    public Object accept(PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }
}
