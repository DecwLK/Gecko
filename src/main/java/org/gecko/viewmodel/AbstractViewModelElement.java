package org.gecko.viewmodel;

import lombok.Getter;
import lombok.NonNull;
import org.gecko.model.Element;

public abstract class AbstractViewModelElement<T extends Element> {
    @Getter
    protected final T target;

    public AbstractViewModelElement(@NonNull T target) {
        this.target = target;
    }

    public abstract void updateTarget();
}
