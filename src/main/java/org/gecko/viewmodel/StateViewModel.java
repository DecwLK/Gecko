package org.gecko.viewmodel;

import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.model.State;

@Setter
@Getter
public class StateViewModel extends BlockViewModelElement<State> {
    private final BooleanProperty isStartStateProperty;
    private final ListProperty<ContractViewModel> contractsProperty;

    public StateViewModel(int id, @NonNull State target) {
        super(id, target);
        this.isStartStateProperty = new SimpleBooleanProperty();
        this.contractsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    public boolean getIsStartState() {
        return isStartStateProperty.getValue();
    }

    public void setStartState(boolean isStartState) {
        isStartStateProperty.setValue(isStartState);
    }

    @Override
    public void updateTarget() {
        super.updateTarget();
        // TODO: not possible because of missing reference to parent system
        /*Automaton automaton = parentSystem.getAutomaton();
        if (getIsStartState()) {
            automaton.setStartState(target);
        }*/
        target.getContracts().clear();
        target.addContracts(contractsProperty.stream().map(ContractViewModel::getTarget).collect(Collectors.toSet()));
    }

    public void addContract(@NonNull ContractViewModel contract) {
        // TODO: prior checks
        contractsProperty.add(contract);
    }

    public void removeContract(@NonNull ContractViewModel contract) {
        contractsProperty.remove(contract);
    }

    @Override
    public Object accept(@NonNull PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }
}
