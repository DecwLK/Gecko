package org.gecko.viewmodel;

import lombok.Getter;
import org.gecko.model.Element;

public abstract class AbstractViewModelElement<T extends Element> {
    @Getter
    protected final T target;

    public AbstractViewModelElement(T target) {
        this.target = target;
    }

    public abstract void updateTarget();
}
