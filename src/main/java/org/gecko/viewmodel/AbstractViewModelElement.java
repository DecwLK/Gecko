package org.gecko.viewmodel;

import org.gecko.model.Element;

public abstract class AbstractViewModelElement<T extends Element> {
    private final T target;

    public AbstractViewModelElement(T target) {
        this.target = target;
    }
    abstract public void updateTarget();
}
