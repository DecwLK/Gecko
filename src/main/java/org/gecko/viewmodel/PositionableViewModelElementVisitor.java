package org.gecko.viewmodel;

/**
 * Represents a visitor pattern for performing operations on {@link PositionableViewModelElement}s. Concrete visitors
 * must implement this interface to define specific behavior for each {@link PositionableViewModelElement}.
 */
public interface PositionableViewModelElementVisitor<T> {
    T visit(SystemViewModel systemViewModel);

    T visit(RegionViewModel regionViewModel);

    T visit(SystemConnectionViewModel systemConnectionViewModel);

    T visit(EdgeViewModel edgeViewModel);

    T visit(StateViewModel stateViewModel);

    T visit(PortViewModel portViewModel);
}
