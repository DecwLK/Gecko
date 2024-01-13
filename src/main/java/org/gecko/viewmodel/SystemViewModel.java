package org.gecko.viewmodel;

import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.System;

@Getter @Setter
public class SystemViewModel extends BlockViewModelElement<System> {
   private StringProperty code;
   private PortViewModel ports;

    public SystemViewModel(System target) {
        super(target);
    }
}
