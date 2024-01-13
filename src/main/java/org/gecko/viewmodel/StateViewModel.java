package org.gecko.viewmodel;

import javafx.beans.property.BooleanProperty;
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
    }
}
