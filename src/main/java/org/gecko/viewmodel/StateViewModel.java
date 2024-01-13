package org.gecko.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.State;

@Getter @Setter
public class StateViewModel extends BlockViewModelElement<State> {
    private BooleanProperty isStartState;
    private ObservableList<ContractViewModel> contracts;

    public StateViewModel(State target) {
        super(target);
        super.setName(target.getName());
        isStartState = new SimpleBooleanProperty(false);
        this.contracts = FXCollections.observableArrayList();
    }

    @Override
    public void updateTarget() {
        // TODO
    }

    public void addContract(ContractViewModel contract) {
        // TODO: prior checks
        this.contracts.add(contract);
    }

    @Override
    public void accept(PositionableViewModelElementVisitor visitor) {
        visitor.visit(this);
    }
}
