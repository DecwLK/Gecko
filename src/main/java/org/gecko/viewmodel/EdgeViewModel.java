package org.gecko.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Contract;
import org.gecko.model.Edge;
import org.gecko.model.Kind;
import org.gecko.model.State;

@Setter @Getter
public class EdgeViewModel<T extends Edge> extends PositionableViewModelElement<T> {
    private Property<Kind> kind;

    private IntegerProperty priority;

    private ContractViewModel<Contract> contract;

    private StateViewModel<State> source;

    private StateViewModel<State> destination;

    public EdgeViewModel(T target) {
        super(target);
    }
}
