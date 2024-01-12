package org.gecko.viewmodel;

public interface PositionableViewModelElementVisitor {
    void visit(SystemViewModel systemViewModel);
    void visit(RegionViewModel regionViewModel);
    void visit(SystemConnectionViewModel systemConnectionViewModel);
    void visit(EdgeViewModel edgeViewModel);
    void visit(StateViewModel stateViewModel);
    void visit(PortViewModel portViewModel);
}
