package org.gecko.actions;

import org.gecko.viewmodel.PositionableViewModelElement;

public abstract class AbstractPositionableViewModelElementAction extends Action {
    abstract PositionableViewModelElement<?> getTarget();
}
