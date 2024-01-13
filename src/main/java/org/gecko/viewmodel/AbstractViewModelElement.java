package org.gecko.viewmodel;

import lombok.Getter;
import org.gecko.model.Element;

@Getter
public abstract class AbstractViewModelElement<T extends Element> {
    private final T target;

    public AbstractViewModelElement(T target) {
        this.target = target;
    }

    public abstract void updateTarget();
}
