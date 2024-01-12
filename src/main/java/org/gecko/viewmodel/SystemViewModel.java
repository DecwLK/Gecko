package org.gecko.viewmodel;

import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.System;
import org.gecko.model.Variable;

@Getter @Setter
public class SystemViewModel<T extends System> extends BlockViewModelElement<T> {
   private StringProperty code;
   private PortViewModel<Variable> ports;

    public SystemViewModel(T target) {
        super(target);
    }
}
