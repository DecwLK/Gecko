package org.gecko.model;

import org.gecko.exceptions.ModelException;

/**
 * Concrete implementation of an {@link ElementVisitor}. A {@link DeleteElementVisitor} focuses on removing
 * {@link Element}s from a given parent-{@link System}.
 */
public class DeleteElementVisitor implements ElementVisitor {

    private final System parentSystem;

    public DeleteElementVisitor(System parentSystem) {
        this.parentSystem = parentSystem;
    }

    @Override
    public void visit(State state) {
        try {
            parentSystem.getAutomaton().removeState(state);
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visit(Contract contract) {
        State state = parentSystem.getAutomaton().getStateWithContract(contract);
        if (state != null) {
            state.removeContract(contract);
        }
    }

    @Override
    public void visit(SystemConnection systemConnection) {
        parentSystem.removeConnection(systemConnection);
    }

    @Override
    public void visit(Variable variable) {
        parentSystem.removeVariable(variable);
    }

    @Override
    public void visit(System system) {
        parentSystem.removeChild(system);
    }

    @Override
    public void visit(Region region) {
        parentSystem.getAutomaton().removeRegion(region);
    }

    @Override
    public void visit(Edge edge) {
        parentSystem.getAutomaton().removeEdge(edge);
    }
}
