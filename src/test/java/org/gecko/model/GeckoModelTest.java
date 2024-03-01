package org.gecko.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.gecko.exceptions.ModelException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GeckoModelTest {
    static GeckoModel defaultModel;
    static GeckoModel modelFromRoot;
    static System root;
    static System child;
    static System childOfChild;
    static Variable variable;

    @BeforeAll
    static void setUp() {
        assertDoesNotThrow(() -> defaultModel = new GeckoModel());

        assertThrows(NullPointerException.class, () -> modelFromRoot = new GeckoModel(null));
        assertDoesNotThrow(() -> root = new System(0, "root", null, new Automaton()));
        assertDoesNotThrow(() -> modelFromRoot = new GeckoModel(root));

        assertDoesNotThrow(() -> child = new System(1, "child", null, new Automaton()));
        assertDoesNotThrow(() -> childOfChild = new System(2, "childOfChild", null, new Automaton()));
        child.addChild(childOfChild);

        assertDoesNotThrow(() -> variable = new Variable(3, "variable", "type", Visibility.INPUT));
        childOfChild.addVariable(variable);


    }

    @Test
    void getAllSystems() {
        assertFalse(defaultModel.getAllSystems().isEmpty());
        assertEquals(1, defaultModel.getAllSystems().size());
        assertEquals("Element_0", defaultModel.getAllSystems().getFirst().getName());

        defaultModel.getRoot().addChild(child);
        assertNotEquals(1, defaultModel.getAllSystems().size());
        assertTrue(defaultModel.getAllSystems().contains(childOfChild));

        defaultModel.getRoot().removeChild(child);
    }

    @Test
    void getSystemWithVariable() {
        defaultModel.getRoot().addChild(child);
        assertThrows(NullPointerException.class, () -> defaultModel.getSystemWithVariable(null));
        assertEquals(childOfChild, defaultModel.getSystemWithVariable(variable));
        defaultModel.getRoot().removeChild(child);
    }

    @Test
    void isNameUnique() {
        assertThrows(NullPointerException.class, () -> defaultModel.isNameUnique(null));

        defaultModel.getRoot().addChild(child);
        assertFalse(defaultModel.isNameUnique("childOfChild"));

        defaultModel.getRoot().addVariable(variable);
        assertFalse(() -> defaultModel.isNameUnique("variable"));

        try {
            Condition condition = new Condition("true");
            Contract contract1 = new Contract(4, "contract1", condition, condition);
            Contract contract2 = new Contract(5, "contract2", condition, condition);

            State state = new State(6, "state");
            state.addContract(contract2);

            defaultModel.getRoot().getAutomaton().addRegion(new Region(7, "region", condition, contract1));
            defaultModel.getRoot().getAutomaton().addState(state);

        } catch (ModelException e) {
            fail("Could not initialize region or state for testing purposes of the model.");
        }

        assertFalse(() -> defaultModel.isNameUnique("region"));
        assertFalse(() -> defaultModel.isNameUnique("state"));
        assertFalse(() -> defaultModel.isNameUnique("contract1"));
        assertFalse(() -> defaultModel.isNameUnique("contract2"));
    }
}
