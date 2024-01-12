package org.gecko.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Contract;
import org.gecko.model.State;
import java.util.List;

@Getter @Setter
public class StateViewModel<T extends State> extends BlockViewModelElement<T> {
    private BooleanProperty isStartState;
    private ObservableList<ContractViewModel<Contract>> contracts;

    public StateViewModel(T target) {
        super(target);
    }
}
