package org.gecko.viewmodel;

import org.gecko.model.Element;
import javafx.beans.property.Property;

// TODO: Extend T with Variable.
public class PortViewModel<T extends Element> extends BlockViewModelElement<T> {
    // TODO: private Property<Visibility> visibility;

    public PortViewModel(T target) {
        super(target);
    }
}
