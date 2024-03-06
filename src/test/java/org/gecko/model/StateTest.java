package org.gecko.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.gecko.exceptions.ModelException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class StateTest {
    static State state;
    static Contract contract1;
    static Contract contract2;

    @BeforeAll
    static void setUp() {
        assertThrows(NullPointerException.class, () -> state = new State(0, null));
        assertThrows(ModelException.class, () -> state = new State(0, ""));
        assertDoesNotThrow(() -> state = new State(0, "state"));

        assertDoesNotThrow(
            () -> contract1 = new Contract(1, "contract1", new Condition("true"), new Condition("true")));
        assertDoesNotThrow(
            () -> contract2 = new Contract(2, "contract2", new Condition("false"), new Condition("false")));
    }

    @Test
    void setName() {
        assertNotNull(state.getName());

        assertThrows(NullPointerException.class, () -> state.setName(null));
        assertThrows(ModelException.class, () -> state.setName(""));
        assertDoesNotThrow(() -> state.setName("newName"));

        assertNotNull(state.getName());
        assertEquals("newName", state.getName());
    }

    @Test
    void manageContracts() {
        assertTrue(state.getContracts().isEmpty());

        state.addContract(contract1);
        assertFalse(state.getContracts().isEmpty());

        state.addContract(contract2);
        assertEquals(2, state.getContracts().size());

        assertDoesNotThrow(() -> state.removeContract(contract1));
        assertEquals(1, state.getContracts().size());
        assertFalse(state.getContracts().contains(contract1));

        assertDoesNotThrow(() -> state.removeContract(contract2));
        assertTrue(state.getContracts().isEmpty());

        Set<Contract> contracts = new HashSet<>();
        contracts.add(contract1);
        contracts.add(contract2);

        state.addContracts(contracts);
        assertEquals(2, state.getContracts().size());
        assertDoesNotThrow(() -> state.removeContracts(contracts));
        assertTrue(state.getContracts().isEmpty());

        assertThrows(NullPointerException.class, () -> state.addContract(null));
        assertThrows(NullPointerException.class, () -> state.addContracts(null));
        assertThrows(NullPointerException.class, () -> state.removeContract(null));
        assertThrows(NullPointerException.class, () -> state.removeContracts(null));
    }
}
