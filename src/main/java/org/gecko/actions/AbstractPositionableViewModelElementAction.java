package org.gecko.actions;

import org.gecko.viewmodel.PositionableViewModelElement;

/**
 * An abstract representation of an {@link Action} that has a target-{@link PositionableViewModelElement}.
 */
public abstract class AbstractPositionableViewModelElementAction extends Action {
    abstract PositionableViewModelElement<?> getTarget();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractPositionableViewModelElementAction action)) {
            return false;
        }
        return getTarget().equals(action.getTarget());
    }

    @Override
    public int hashCode() {
        return getTarget().hashCode();
    }
}
