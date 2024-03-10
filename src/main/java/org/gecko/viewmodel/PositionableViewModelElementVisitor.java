package org.gecko.viewmodel;

/**
 * Represents a visitor pattern for performing operations on {@link PositionableViewModelElement}s. Concrete visitors
 * must implement this interface to define specific behavior for each {@link PositionableViewModelElement}.
 */
public interface PositionableViewModelElementVisitor {
    Object visit(SystemViewModel systemViewModel);

    Object visit(RegionViewModel regionViewModel);

    Object visit(SystemConnectionViewModel systemConnectionViewModel);

    Object visit(EdgeViewModel edgeViewModel);

    Object visit(StateViewModel stateViewModel);

    Object visit(PortViewModel portViewModel);
}
