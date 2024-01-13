package org.gecko.viewmodel;

import lombok.Data;
import org.gecko.model.Element;

@Data
public abstract class AbstractViewModelElement<T extends Element> {
    private final T target;

    public AbstractViewModelElement(T target) {
        this.target = target;
    }

    abstract public void updateTarget();
}
