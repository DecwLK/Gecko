package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import org.gecko.model.Element;
import javafx.scene.paint.Color;

// TODO: Extend T with Region.
public class RegionViewModel<T extends Element> extends BlockViewModelElement<T> {
    private Property<Color> color;
    private ContractViewModel<T> contract;
    private StringProperty invariant;

    public RegionViewModel(T target) {
        super(target);
    }

    public void addState(StateViewModel<T> state) {
        // TODO
    }
}
