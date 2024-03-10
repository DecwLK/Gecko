package org.gecko.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.gecko.exceptions.ModelException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ModelFactoryTest {
    GeckoModel model;
    ModelFactory factory;

    @BeforeEach
    void setUp() {
        try {
            model = new GeckoModel();
        } catch (ModelException e) {
            fail("Could not initialize model for testing purposes of the model factory.");
        }

        factory = new ModelFactory(model);
    }

    @Test
    void createState() {
        assertThrows(NullPointerException.class, () -> factory.createState(null));
        assertDoesNotThrow(() -> factory.createState(new Automaton()));
    }

    @Test
    void createEdge() {
        assertThrows(NullPointerException.class,
            () -> factory.createEdge(null, new State(0, "source"), new State(1, "destination")));
        assertThrows(NullPointerException.class,
            () -> factory.createEdge(new Automaton(), null, new State(1, "destination")));
        assertThrows(NullPointerException.class,
            () -> factory.createEdge(new Automaton(), new State(0, "source"), null));

        Automaton automaton = new Automaton();
        State source = null;
        State destination = null;
        try {
            source = new State(0, "source");
            destination = new State(1, "destination");
        } catch (ModelException e) {
            fail("Failed to create source and destination states for testing purposes of the creation of an edge.");
        }
        assertNotNull(source);
        assertNotNull(destination);
        automaton.addState(source);
        automaton.addState(destination);

        try {
            factory.createEdge(automaton, source, destination);
        } catch (ModelException e) {
            fail("Failed to create edge from valid source and destination states.");
        }
    }

    @Test
    void createSystem() {
        assertThrows(NullPointerException.class, () -> factory.createSystem(null));
        assertDoesNotThrow(() -> factory.createSystem(new System(0, "parent", null, new Automaton())));
    }

    @Test
    void createVariable() {
        assertThrows(NullPointerException.class, () -> factory.createVariable(null));
        assertDoesNotThrow(() -> factory.createVariable(new System(0, "system", null, new Automaton())));
    }

    @Test
    void createSystemConnection() {
        System system = null;
        System child1;
        System child2;
        Variable variable1 = null;
        Variable variable2 = null;
        try {
            system = new System(0, "system", null, new Automaton());
            child1 = new System(1, "child1", null, new Automaton());
            child2 = new System(2, "child2", null, new Automaton());
            system.addChild(child1);
            child1.setParent(system);
            system.addChild(child2);
            child2.setParent(system);

            variable1 = new Variable(3, "var1", "type", Visibility.OUTPUT);
            variable2 = new Variable(4, "var2", "type", Visibility.INPUT);

            child1.addVariable(variable1);
            child2.addVariable(variable2);
        } catch (ModelException e) {
            fail("Failed to create elements for testing purposes of a system connection.");
        }

        assertNotNull(system);
        model.getRoot().addChild(system);
        system.setParent(model.getRoot());

        SystemConnection systemConnection = null;
        try {
            systemConnection = factory.createSystemConnection(null, variable1, variable2);
        } catch (NullPointerException e) {
            assertNull(systemConnection);
        } catch (ModelException e) {
            fail("NullPointerException should be thrown before the model intervenes.");
        }

        try {
            systemConnection = factory.createSystemConnection(system, null, variable2);
        } catch (NullPointerException e) {
            assertNull(systemConnection);
        } catch (ModelException e) {
            fail("NullPointerException should be thrown before the model intervenes.");
        }

        try {
            systemConnection = factory.createSystemConnection(system, variable1, null);
        } catch (NullPointerException e) {
            assertNull(systemConnection);
        } catch (ModelException e) {
            fail("NullPointerException should be thrown before the model intervenes.");
        }

        assertNull(systemConnection);
        try {
            factory.createSystemConnection(system, variable1, variable2);
        } catch (ModelException e) {
            fail("System connection creation failed.");
        }
    }

    @Test
    void createContract() {
        assertThrows(NullPointerException.class, () -> factory.createContract(null));
        assertDoesNotThrow(() -> factory.createContract(new State(0, "state")));
    }

    @Test
    void createRegion() {
        assertThrows(NullPointerException.class, () -> factory.createRegion(null));
        assertDoesNotThrow(() -> factory.createRegion(new Automaton()));
    }

    @Test
    void createCondition() {
        assertThrows(NullPointerException.class, () -> factory.createCondition(null));
        assertDoesNotThrow(() -> factory.createCondition("true"));
    }

    @Test
    void getDefaultName() {
        final System[] children = new System[2];

        try {
            children[0] = new System(1, "Element_1", null, new Automaton());
        } catch (ModelException e) {
            fail("Could not initialize system for testing purposes of getting the right default name.");
        }
        model.getRoot().addChild(children[0]);

        assertDoesNotThrow(() -> children[1] = factory.createSystem(model.getRoot()));
        assertNotEquals("Element_1", children[1].getName());
        assertEquals("Element_2", children[1].getName());
        assertTrue(model.getRoot().getChildren().contains(children[1]));
    }

    @Test
    void createEdgeBetweenInvalidStates() {
        Automaton automaton = new Automaton();
        State source = null;
        State destination = null;
        State other = null;
        Edge edge = null;
        try {
            source = new State(0, "source");
            destination = new State(1, "destination");
            other = new State(2, "other");
        } catch (ModelException e) {
            fail("Failed to create source and destination states for testing purposes of the creation of an edge.");
        }
        assertNotNull(source);
        assertNotNull(destination);
        assertNotNull(other);

        try { // Empty automaton:
            edge = factory.createEdge(automaton, source, destination);
        } catch (RuntimeException e) {
            assertNull(edge);
        } catch (ModelException e) {
            fail("RuntimeException should be thrown before the model intervenes.");
        }

        automaton.addState(other);
        try { // Source and destination not in automaton:
            edge = factory.createEdge(automaton, source, destination);
        } catch (RuntimeException e) {
            assertNull(edge);
        } catch (ModelException e) {
            fail("RuntimeException should be thrown before the model intervenes.");
        }

        automaton.addState(source);
        try { // Destination not in automaton:
            edge = factory.createEdge(automaton, source, destination);
        } catch (RuntimeException e) {
            assertNull(edge);
        } catch (ModelException e) {
            fail("RuntimeException should be thrown before the model intervenes.");
        }

        automaton.addState(destination);
        try {
            edge = factory.createEdge(automaton, source, destination);
        } catch (ModelException e) {
            fail("Failed to create edge.");
        } catch (RuntimeException e) {
            fail("Failed to connect valid states with a new edge.");
        }

        automaton.addState(source);
        automaton.addState(destination);
    }

    @Test
    void createSystemConnectionBetweenInvalidStates() {
        System parent = null;
        System child1 = null;
        System child2 = null;
        Variable var1 = null;
        Variable var2 = null;
        SystemConnection connection = null;
        try {
            parent = factory.createSystem(model.getRoot());
            child1 = new System(1, "child1", null, new Automaton());
            child2 = new System(2, "child2", null, new Automaton());
            var1 = new Variable(3, "var1", "int", Visibility.INPUT);
            var2 = new Variable(4, "var2", "int", Visibility.INPUT);
        } catch (ModelException e) {
            fail("Failed to create source and destination states for testing purposes of the creation of an edge.");
        }
        assertNotNull(parent);
        assertNotNull(child1);
        assertNotNull(child2);
        assertNotNull(var1);
        assertNotNull(var2);

        try { // Variables with no parents are not part of the model:
            connection = factory.createSystemConnection(parent, var1, var2);
        } catch (RuntimeException e) {
            assertNull(connection);
        } catch (ModelException e) {
            fail("RuntimeException should be thrown before the model intervenes.");
        }
        assertNull(connection);

        parent.addVariable(var1);
        try { // Variable 2 with no parent is not part of the model:
            connection = factory.createSystemConnection(parent, var1, var2);
        } catch (RuntimeException e) {
            assertNull(connection);
        } catch (ModelException e) {
            fail("RuntimeException should be thrown before the model intervenes.");
        }
        assertNull(connection);

        parent.addVariable(var2);
        try { // Variables have the same parent:
            connection = factory.createSystemConnection(parent, var1, var2);
        } catch (ModelException e) {
            assertNull(connection);
        }
        assertNull(connection);

        parent.removeVariable(var2);
        child1.addVariable(var2);
        try { // Valid variables, but child1 is not child of parent:
            connection = factory.createSystemConnection(parent, var1, var2);
        } catch (RuntimeException e) {
            assertNull(connection);
        } catch (ModelException e) {
            fail("RuntimeException should be thrown before the model intervenes.");
        }
        assertNull(connection);

        parent.addChild(child1);
        child1.setParent(parent);
        var2.setVisibility(Visibility.OUTPUT);
        try { // Variable 1 in parent, variable 2 in child, but wrong visibilities:
            connection = factory.createSystemConnection(parent, var1, var2);
        } catch (ModelException e) {
            assertNull(connection);
        }
        assertNull(connection);

        var1.setVisibility(Visibility.OUTPUT);
        try { // Variable 1 in parent, variable 2 in child, but wrong visibilities:
            connection = factory.createSystemConnection(parent, var1, var2);
        } catch (ModelException e) {
            assertNull(connection);
        }
        assertNull(connection);

        parent.removeVariable(var1);
        child2.addVariable(var1);
        try { // Variables in children, but child2 not child of parent:
            connection = factory.createSystemConnection(parent, var2, var1);
        } catch (RuntimeException e) {
            assertNull(connection);
        } catch (ModelException e) {
            fail("RuntimeException should be thrown before the model intervenes.");
        }
        assertNull(connection);

        parent.addChild(child2);
        child2.setParent(parent);
        try { // Variables in children, but wrong visibilities:
            connection = factory.createSystemConnection(parent, var2, var1);
        } catch (ModelException e) {
            assertNull(connection);
        }
        assertNull(connection);

        var2.setVisibility(Visibility.INPUT);
        try {
            connection = factory.createSystemConnection(parent, var1, var2);
        } catch (ModelException e) {
            fail("Failed to create system connection between valid variables.");
        }
    }
}
