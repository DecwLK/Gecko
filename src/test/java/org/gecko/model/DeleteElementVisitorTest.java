package org.gecko.model;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        factory.createContract(automaton.getStates().stream().findFirst().get());
        visitor = new DeleteElementVisitor(parentSystem);
    }

    @Test
    void visitState() {
        State state = parentSystem.getAutomaton().getStates().stream().findFirst().get();
        visitor.visit(state);
        assertFalse(parentSystem.getAutomaton().getStates().contains(state));
    }

    @Test
    void visitContract() {
        State state = parentSystem.getAutomaton().getStates().stream().findFirst().get();
        Contract contract = state.getContracts().stream().findFirst().get();
        visitor.visit(contract);
        assertFalse(state.getContracts().contains(contract));
    }

    @Test
    void visitSystemConnection() {
        SystemConnection connection = parentSystem.getConnections().stream().findFirst().get();
        visitor.visit(connection);
        assertFalse(parentSystem.getConnections().contains(connection));
    }

    @Test
    void visitVariable() {
        Variable variable = parentSystem.getVariables().stream().findFirst().get();
        visitor.visit(variable);
        assertFalse(parentSystem.getVariables().contains(variable));
    }

    @Test
    void visitSystem() {
        System system = parentSystem.getChildren().stream().findFirst().get();
        visitor.visit(system);
        assertFalse(parentSystem.getChildren().contains(system));
    }

    @Test
    void visitRegion() {
        Region region = parentSystem.getAutomaton().getRegions().stream().findFirst().get();
        visitor.visit(region);
        assertFalse(parentSystem.getAutomaton().getRegions().contains(region));
    }

    @Test
    void visitEdge() {
        List<Edge> edges = new ArrayList<>(parentSystem.getAutomaton().getEdges());
        Edge edge = edges.getFirst();
        visitor.visit(edge);
        assertFalse(parentSystem.getAutomaton().getEdges().contains(edge));
    }
}