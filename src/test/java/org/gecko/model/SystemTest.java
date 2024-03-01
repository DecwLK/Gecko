package org.gecko.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.gecko.exceptions.ModelException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SystemTest {
    static System system;
    static System child1;
    static System child2;
    static Variable variable1;
    static Variable variable2;
    static SystemConnection connection1;
    static SystemConnection connection2;
    static Variable childVariable1;
    static Variable childVariable2;

    @BeforeAll
    static void setUp() {
        assertThrows(NullPointerException.class,
            () -> system = new System(0, null, null, new Automaton()));
        assertThrows(ModelException.class, () -> system = new System(0, "", null, new Automaton()));

        assertThrows(ModelException.class, () -> system = new System(0, "system", "", new Automaton()));

        assertThrows(NullPointerException.class,
            () -> system = new System(0, "system", null, null));

        assertDoesNotThrow(() -> system = new System(0, "system", null, new Automaton()));
        assertDoesNotThrow(() -> child1 = new System(1, "child1", null, new Automaton()));
        assertDoesNotThrow(() -> child2 = new System(2, "child2", null, new Automaton()));

        assertDoesNotThrow(() -> variable1 = new Variable(3, "var1", "type", Visibility.INPUT));
        assertDoesNotThrow(() -> variable2 = new Variable(4, "var2", "type", Visibility.OUTPUT));

        assertDoesNotThrow(() -> childVariable1 = new Variable(5, "childVar1", "type", Visibility.INPUT));
        assertDoesNotThrow(() -> childVariable2 = new Variable(6, "childVar2", "type", Visibility.OUTPUT));

        assertDoesNotThrow(() -> connection1 = new SystemConnection(7, childVariable2, childVariable1));
        assertDoesNotThrow(() -> connection2 = new SystemConnection(8, childVariable2, childVariable1));
    }

    @Test
    void testInitializedSystem() {
        assertNotNull(system.getName());
        assertTrue(system.getCode() == null || !system.getCode().isEmpty());
        assertNotNull(system.getAutomaton());

        assertNotNull(system.getChildren());
        assertNotNull(system.getConnections());
        assertNotNull(system.getVariables());

        assertTrue(system.getChildren().isEmpty());
        assertTrue(system.getConnections().isEmpty());
        assertTrue(system.getVariables().isEmpty());
    }

    @Test
    void setName() {
        assertNotNull(system.getName());

        assertThrows(NullPointerException.class, () -> system.setName(null));
        assertThrows(ModelException.class, () -> system.setName(""));
        assertDoesNotThrow(() -> system.setName("newName"));

        assertNotNull(system.getName());
        assertEquals("newName", system.getName());
    }

    @Test
    void setCode() {
        assertNull(system.getCode());

        assertDoesNotThrow(() -> system.setCode(null));
        assertThrows(ModelException.class, () -> system.setCode(""));
        assertDoesNotThrow(() -> system.setCode("newCode"));

        assertNotNull(system.getCode());
        assertEquals("newCode", system.getCode());
    }

    @Test
    void manageChildren() {
        assertTrue(system.getChildren().isEmpty());

        system.addChild(child1);
        assertFalse(system.getChildren().isEmpty());

        system.addChild(child2);
        assertEquals(2, system.getChildren().size());

        assertDoesNotThrow(() -> system.removeChild(child1));
        assertEquals(1, system.getChildren().size());
        assertFalse(system.getChildren().contains(child1));

        assertDoesNotThrow(() -> system.removeChild(child2));
        assertTrue(system.getChildren().isEmpty());

        Set<System> children = new HashSet<>();
        children.add(child1);
        children.add(child2);

        system.addChildren(children);
        assertEquals(2, system.getChildren().size());
        assertDoesNotThrow(() -> system.removeChildren(children));
        assertTrue(system.getChildren().isEmpty());

        assertThrows(NullPointerException.class, () -> system.addChild(null));
        assertThrows(NullPointerException.class, () -> system.addChildren(null));
        assertThrows(NullPointerException.class, () -> system.removeChild(null));
        assertThrows(NullPointerException.class, () -> system.removeChildren(null));
    }

    @Test
    void getAllChildren() {
        assertTrue(system.getAllChildren().isEmpty());

        Set<System> children = new HashSet<>();
        children.add(child1);
        children.add(child2);

        system.addChildren(children);
        assertEquals(children.stream().toList(), system.getAllChildren());
        system.removeChild(child2);
        assertNotEquals(children.stream().toList(), system.getAllChildren());
        system.addChild(child1);
        assertEquals(1, system.getAllChildren().size());
    }

    @Test
    void getChildSystemWithVariable() {
        child2.addVariable(childVariable1);
        system.addChild(child1);
        system.addChild(child2);

        assertEquals(child2, system.getChildSystemWithVariable(childVariable1));
        assertDoesNotThrow(() -> system.removeChild(child2));
        assertNull(system.getChildSystemWithVariable(childVariable1));
        assertDoesNotThrow(() -> system.removeChild(child1));
    }

    @Test
    void manageConnections() {
        assertTrue(system.getConnections().isEmpty());

        system.addConnection(connection1);
        assertFalse(system.getConnections().isEmpty());

        system.addConnection(connection2);
        assertEquals(2, system.getConnections().size());

        system.removeConnection(connection1);
        assertEquals(1, system.getConnections().size());
        assertFalse(system.getConnections().contains(connection1));

        system.removeConnection(connection2);
        assertTrue(system.getConnections().isEmpty());

        Set<SystemConnection> connections = new HashSet<>();
        connections.add(connection1);
        connections.add(connection2);

        system.addConnections(connections);
        assertEquals(2, system.getConnections().size());
        assertDoesNotThrow(() -> system.removeConnections(connections));
        assertTrue(system.getConnections().isEmpty());

        assertThrows(NullPointerException.class, () -> system.addConnection(null));
        assertThrows(NullPointerException.class, () -> system.addConnections(null));
        assertThrows(NullPointerException.class, () -> system.removeConnection(null));
        assertThrows(NullPointerException.class, () -> system.removeConnections(null));
    }

    @Test
    void manageVariables() {
        assertTrue(system.getVariables().isEmpty());

        system.addVariable(variable1);
        assertFalse(system.getVariables().isEmpty());

        system.addVariable(variable2);
        assertEquals(2, system.getVariables().size());

        assertDoesNotThrow(() -> system.removeVariable(variable1));
        assertEquals(1, system.getVariables().size());
        assertFalse(system.getVariables().contains(variable1));

        assertDoesNotThrow(() -> system.removeVariable(variable2));
        assertTrue(system.getVariables().isEmpty());

        Set<Variable> variables = new HashSet<>();
        variables.add(variable1);
        variables.add(variable2);

        system.addVariables(variables);
        assertEquals(2, system.getVariables().size());
        assertDoesNotThrow(() -> system.removeVariables(variables));
        assertTrue(system.getVariables().isEmpty());

        assertThrows(NullPointerException.class, () -> system.addVariable(null));
        assertThrows(NullPointerException.class, () -> system.addVariables(null));
        assertThrows(NullPointerException.class, () -> system.removeVariable(null));
        assertThrows(NullPointerException.class, () -> system.removeVariables(null));
    }

    @Test
    void getAllElements() {
        assertTrue(system.getAllElements().isEmpty());

        system.addChild(child1);
        system.addVariable(variable1);
        system.addConnection(connection1);
        assertEquals(3, system.getAllElements().size());

        system.addVariable(variable1);
        assertEquals(3, system.getAllElements().size());

        system.removeChild(child1);
        system.removeVariable(variable1);
        system.removeConnection(connection1);
    }
}
