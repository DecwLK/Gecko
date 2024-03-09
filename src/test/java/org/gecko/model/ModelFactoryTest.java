package org.gecko.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.gecko.exceptions.ModelException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ModelFactoryTest {
    static GeckoModel model;
    static ModelFactory factory;

    @BeforeAll
    static void setUp() {
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

        assertDoesNotThrow(
            () -> factory.createEdge(new Automaton(), new State(0, "source"), new State(1, "destination")));
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
        assertThrows(NullPointerException.class,
            () -> factory.createSystemConnection(null, new Variable(1, "var1", "type", Visibility.OUTPUT),
                new Variable(2, "var2", "type", Visibility.INPUT)));
        assertThrows(NullPointerException.class,
            () -> factory.createSystemConnection(new System(0, "system", null, new Automaton()), null,
                new Variable(2, "var2", "type", Visibility.INPUT)));
        assertThrows(NullPointerException.class,
            () -> factory.createSystemConnection(new System(0, "system", null, new Automaton()),
                new Variable(1, "var1", "type", Visibility.OUTPUT), null));

        assertDoesNotThrow(
            () -> factory.createSystemConnection(new System(0, "system", null, new Automaton()),
                new Variable(1, "var1", "type", Visibility.OUTPUT),
                new Variable(2, "var2", "type", Visibility.INPUT)));
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
}
