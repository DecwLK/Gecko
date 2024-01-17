package org.gecko.view.views.viewelement;

public interface ViewElementVisitor {
    void visit(StateViewElement stateViewElement);
    void visit(EdgeViewElement edgeViewElement);
    void visit(RegionViewElement regionViewElement);
    void visit(SystemViewElement systemViewElement);
    void visit(SystemConnectionViewElement systemConnectionViewElement);
    void visit(VariableBlockViewElement variableBlockViewElement);
}
