package org.gecko.viewmodel;

import javafx.beans.property.BooleanProperty;
import org.gecko.model.Element;

import java.util.List;

// TODO: Extend T with State.
public class StateViewModel<T extends Element> extends BlockViewModelElement<T> {
    private BooleanProperty isStartState;
    private List<ContractViewModel<T>> contracts;

    public StateViewModel(T target) {
        super(target);
    }
}
