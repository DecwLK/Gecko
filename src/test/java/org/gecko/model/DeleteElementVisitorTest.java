package org.gecko.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeleteElementVisitorTest {

    System parentSystem;
    DeleteElementVisitor visitor;

    @BeforeEach
    void setUp() {
        //Initialize a basic model that has one of everything
        GeckoModel model = new GeckoModel();
        ModelFactory factory = model.getModelFactory();
        parentSystem = model.getRoot();
        factory.createSystem(parentSystem);
        Variable var1 = factory.createVariable(parentSystem);
        Variable var2 = factory.createVariable(parentSystem);
        factory.createSystemConnection(parentSystem, var1, var2);
        Automaton automaton = factory.createAutomaton(parentSystem);
        factory.createRegion(automaton);
        State state1 = factory.createState(automaton);
        State state2 = factory.createState(automaton);
        factory.createEdge(automaton, state1, state2);
        factory.createContract(automaton.getStates().getFirst());
        visitor = new DeleteElementVisitor(parentSystem);
    }

    @Test
    void visitState() {
        State state = parentSystem.getAutomaton().getStates().getFirst();
        assertTrue(parentSystem.getAutomaton().getStates().contains(state));
        visitor.visit(state);
        assertFalse(parentSystem.getAutomaton().getStates().contains(state));
    }

    @Test
    void visitContract() {
        State state = parentSystem.getAutomaton().getStates().getFirst();
        Contract contract = state.getContracts().getFirst();
        assertTrue(state.getContracts().contains(contract));
        visitor.visit(contract);
        assertFalse(state.getContracts().contains(contract));
    }

    @Test
    void visitSystemConnection() {
        SystemConnection connection = parentSystem.getConnections().getFirst();
        assertTrue(parentSystem.getConnections().contains(connection));
        visitor.visit(connection);
        assertFalse(parentSystem.getConnections().contains(connection));
    }

    @Test
    void visitVariable() {
        Variable variable = parentSystem.getVariables().getFirst();
        assertTrue(parentSystem.getVariables().contains(variable));
        visitor.visit(variable);
        assertFalse(parentSystem.getVariables().contains(variable));
    }

    @Test
    void visitSystem() {
        System system = parentSystem.getChildren().getFirst();
        assertTrue(parentSystem.getChildren().contains(system));
        visitor.visit(system);
        assertFalse(parentSystem.getChildren().contains(system));
    }

    @Test
    void visitRegion() {
        Region region = parentSystem.getAutomaton().getRegions().getFirst();
        assertTrue(parentSystem.getAutomaton().getRegions().contains(region));
        visitor.visit(region);
        assertFalse(parentSystem.getAutomaton().getRegions().contains(region));
    }

    @Test
    void visitEdge() {
        Edge edge = parentSystem.getAutomaton().getEdges().getFirst();
        assertTrue(parentSystem.getAutomaton().getEdges().contains(edge));
        visitor.visit(edge);
        assertFalse(parentSystem.getAutomaton().getEdges().contains(edge));
    }
}