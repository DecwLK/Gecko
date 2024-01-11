package org.gecko.viewmodel;

import org.gecko.model.Element;

// TODO: Extend T with SystemConnection.
public class SystemConnectionViewModel<T extends Element> extends PositionableViewModelElement<T> {
    private PortViewModel<T> source;
    private PortViewModel<T> destination;

    SystemConnectionViewModel(T target) {
        super(target);
    }
}
