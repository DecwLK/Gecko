package org.gecko.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import org.gecko.model.Element;

// TODO: Extend T with Edge.
public class EdgeViewModel<T extends Element> extends PositionableViewModelElement<T> {
    // TODO: private Property<Kind> kind;
    private IntegerProperty priority;
    private ContractViewModel<T> contract;
    private StateViewModel<T> source;
    private StateViewModel<T> destination;

    public EdgeViewModel(T target) {
        super(target);
    }
}
