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
    /**
     * The target-{@link Element} in the model of this {@link AbstractViewModelElement}.
     */
    protected final T target;
    /**
     * The unique identifier of this {@link AbstractViewModelElement}.
     */
    protected final int id;

    public AbstractViewModelElement(int id, @NonNull T target) {
        this.id = id;
        this.target = target;
    }

    /**
     * Updates the target-{@link Element} in the model with the data of this {@link AbstractViewModelElement}. It has to
     * be called after the data of this {@link AbstractViewModelElement} has been updated to keep the model consistent
     * with the view model.
     *
     * @throws ModelException if the update fails because of a change, which is not allowed in the model
     */
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
