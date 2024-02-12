package org.gecko.actions;

import org.gecko.viewmodel.PositionableViewModelElement;

/**
 * An abstract representation of an {@link Action} that has a target-{@link PositionableViewModelElement}.
 */
public abstract class AbstractPositionableViewModelElementAction extends Action {
    abstract PositionableViewModelElement<?> getTarget();
}
