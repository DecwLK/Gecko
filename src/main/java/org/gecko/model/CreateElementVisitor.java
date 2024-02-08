package org.gecko.model;

import org.gecko.exceptions.ModelException;

/**
 * Concrete implementation of an {@link ElementVisitor}. A {@link CreateElementVisitor} focuses on creating new
 * {@link Element}s in a given parent-{@link System}.
 */
public class CreateElementVisitor implements ElementVisitor {

    private final System parentSystem;

    public CreateElementVisitor(System parentSystem) {
        this.parentSystem = parentSystem;
    }

    @Override
    public void visit(State state) throws ModelException {
        parentSystem.getAutomaton().addState(state);
    }

    @Override
    public void visit(Contract contract) {

    }

    @Override
    public void visit(SystemConnection systemConnection) throws ModelException {
        parentSystem.addConnection(systemConnection);
    }

    @Override
    public void visit(Variable variable) throws ModelException {
        parentSystem.addVariable(variable);
    }

    @Override
    public void visit(System system) throws ModelException {
        parentSystem.addChild(system);
    }

    @Override
    public void visit(Region region) throws ModelException {
        parentSystem.getAutomaton().addRegion(region);
    }

    @Override
    public void visit(Edge edge) throws ModelException {
        parentSystem.getAutomaton().addEdge(edge);
    }
}
