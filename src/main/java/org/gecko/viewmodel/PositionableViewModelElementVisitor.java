package org.gecko.viewmodel;

public interface PositionableViewModelElementVisitor {
    Object visit(SystemViewModel systemViewModel);
    Object visit(RegionViewModel regionViewModel);
    Object visit(SystemConnectionViewModel systemConnectionViewModel);
    Object visit(EdgeViewModel edgeViewModel);
    Object visit(StateViewModel stateViewModel);
    Object visit(PortViewModel portViewModel);
}
