package org.gecko.viewmodel;

import javafx.beans.property.StringProperty;
import org.gecko.model.Element;

// TODO: Extend T with System.
public class SystemViewModel<T extends Element> extends BlockViewModelElement<T> {
   private StringProperty code;
   private PortViewModel<T> ports;

    public SystemViewModel(T target) {
        super(target);
    }
}
