package org.gecko.viewmodel;

import lombok.Getter;
import lombok.Setter;
import org.gecko.model.SystemConnection;
import org.gecko.model.Variable;

@Getter @Setter
public class SystemConnectionViewModel<T extends SystemConnection> extends PositionableViewModelElement<T> {
    private PortViewModel<Variable> source;
    private PortViewModel<Variable> destination;

    SystemConnectionViewModel(T target) {
        super(target);
    }
}
