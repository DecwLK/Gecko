package org.gecko.viewmodel;

import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import org.gecko.exceptions.ModelException;
import org.gecko.model.Element;

/**
 * Represents an abstraction of a view model element of a Gecko project. An {@link AbstractViewModelElement} has an id
 * and a target-{@link Element}, the data of which it can update.
 */
@Getter
public abstract class AbstractViewModelElement<T extends Element> {
    protected final T target;
    protected final int id;

    public AbstractViewModelElement(int id, @NonNull T target) {
        this.id = id;
        this.target = target;
    }

    public abstract void updateTarget() throws ModelException;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractViewModelElement<?> element)) {
            return false;
        }
        return id == element.id;
    }
}
