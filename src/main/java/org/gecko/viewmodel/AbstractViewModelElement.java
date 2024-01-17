package org.gecko.viewmodel;

import lombok.Getter;
import lombok.NonNull;
import org.gecko.model.Element;

@Getter
public abstract class AbstractViewModelElement<T extends Element> {
    protected final T target;
    protected final int id;

    public AbstractViewModelElement(int id, @NonNull T target) {
        this.id = id;
        this.target = target;
    }

    public abstract void updateTarget();
}
