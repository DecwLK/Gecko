package org.gecko.model;

/**
 * Represents a visitor pattern for performing operations on {@link Element}s. Concrete visitors must implement this
 * interface to define specific behavior for each {@link Element}.
 */
public interface ElementVisitor {
    void visit(State state);

    void visit(Contract contract);

    void visit(SystemConnection systemConnection);

    void visit(Variable variable);

    void visit(System system);

    void visit(Region region);

    void visit(Edge edge);
}
