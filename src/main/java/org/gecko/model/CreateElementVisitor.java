package org.gecko.model;

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
    public void visit(State state) {
        parentSystem.getAutomaton().addState(state);
    }

    @Override
    public void visit(Contract contract) {

    }

    @Override
    public void visit(SystemConnection systemConnection) {
        parentSystem.addConnection(systemConnection);
    }

    @Override
    public void visit(Variable variable) {
        parentSystem.addVariable(variable);
    }

    @Override
    public void visit(System system) {
        parentSystem.addChild(system);
    }

    @Override
    public void visit(Region region) {
        parentSystem.getAutomaton().addRegion(region);
    }

    @Override
    public void visit(Edge edge) {
        parentSystem.getAutomaton().addEdge(edge);
    }
}
