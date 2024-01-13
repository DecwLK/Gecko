package org.gecko.model;

public interface ElementVisitor {
    void visit(State state);
    void visit(Contract contract);
    void visit(SystemConnection systemConnection);
    void visit(Variable variable);
    void visit(System system);
    void visit(Region region);
    void visit(Edge edge);
}
