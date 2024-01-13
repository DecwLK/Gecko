package org.gecko.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Edge;
import org.gecko.model.Kind;

@Setter @Getter
public class EdgeViewModel extends PositionableViewModelElement<Edge> {
    private Property<Kind> kind;

    private IntegerProperty priority;

    private ContractViewModel contract;

    private StateViewModel source;

    private StateViewModel destination;

    public EdgeViewModel(Edge target) {
        super(target);
    }
}
