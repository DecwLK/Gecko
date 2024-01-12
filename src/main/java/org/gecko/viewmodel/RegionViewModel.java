package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Contract;
import javafx.scene.paint.Color;
import org.gecko.model.Region;
import org.gecko.model.State;

import java.util.List;

@Getter @Setter
public class RegionViewModel<T extends Region> extends BlockViewModelElement<T> {
    private Property<Color> color;
    private ContractViewModel<Contract> contract;
    private StringProperty invariant;
    private List<StateViewModel<State>> states;

    public RegionViewModel(T target) {
        super(target);
    }

    public void addState(StateViewModel<State> state) {
        // TODO
    }
}
