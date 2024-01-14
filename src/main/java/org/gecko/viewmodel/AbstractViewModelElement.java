package org.gecko.viewmodel;

import org.gecko.model.Element;

public abstract class AbstractViewModelElement<T extends Element> {
    protected final T target;

    public AbstractViewModelElement(T target) {
        this.target = target;
    }

    public abstract void updateTarget();
}
