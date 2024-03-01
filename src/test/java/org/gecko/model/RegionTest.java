package org.gecko.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.gecko.exceptions.ModelException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RegionTest {
    static Region regionWithNullConditions;
    static Region regionWithValidConditions;
    static Condition condition;
    static Contract preAndPostCondition;
    static State state1;
    static State state2;

    @BeforeAll
    static void setUp() {
        assertDoesNotThrow(() -> condition = new Condition("true"));
        assertDoesNotThrow(() -> preAndPostCondition = new Contract(0, "contract", condition, condition));

        assertDoesNotThrow(() -> regionWithValidConditions = new Region(2, "region", condition,
            new Contract(3, "preAndPost", condition, condition)));

        assertDoesNotThrow(() -> state1 = new State(4, "state1"));
        assertDoesNotThrow(() -> state2 = new State(5, "state2"));
    }

    @Test
    void testInitializationOfRegionWithNullConditions() {
        assertThrows(NullPointerException.class,
            () -> regionWithNullConditions = new Region(1, "region", condition, null));
        assertThrows(NullPointerException.class,
            () -> regionWithNullConditions = new Region(1, "region", null,
                new Contract(3, "preAndPost", condition, condition)));
    }

    @Test
    void setName() {
        assertThrows(NullPointerException.class, () -> regionWithValidConditions.setName(null));
        assertThrows(ModelException.class, () -> regionWithValidConditions.setName(""));
        assertDoesNotThrow(() -> regionWithValidConditions.setName("newName"));
    }

    @Test
    void setInvariant() {
        assertThrows(NullPointerException.class, () -> regionWithValidConditions.setInvariant(null));
        assertNotNull(regionWithValidConditions.getInvariant());

        assertThrows(ModelException.class, () -> regionWithValidConditions.setInvariant(new Condition("")));
        assertDoesNotThrow(() -> regionWithValidConditions.setInvariant(new Condition("false")));
        assertNotNull(regionWithValidConditions.getInvariant());
    }

    @Test
    void setPreAndPostCondition() {
        assertNotNull(regionWithValidConditions.getPreAndPostCondition());
        assertNotNull(regionWithValidConditions.getPreAndPostCondition().getPreCondition());
        assertNotNull(regionWithValidConditions.getPreAndPostCondition().getPostCondition());

        assertThrows(NullPointerException.class,
            () -> regionWithValidConditions.getPreAndPostCondition().setPreCondition(null));
        assertThrows(NullPointerException.class,
            () -> regionWithValidConditions.getPreAndPostCondition().setPostCondition(null));

        assertNotNull(regionWithValidConditions.getPreAndPostCondition().getPreCondition());
        assertNotNull(regionWithValidConditions.getPreAndPostCondition().getPostCondition());
    }

    @Test
    void manageStates() {
        assertTrue(regionWithValidConditions.getStates().isEmpty());

        regionWithValidConditions.addState(state1);
        assertFalse(regionWithValidConditions.getStates().isEmpty());

        regionWithValidConditions.addState(state2);
        assertEquals(2, regionWithValidConditions.getStates().size());

        assertDoesNotThrow(() -> regionWithValidConditions.removeState(state1));
        assertEquals(1, regionWithValidConditions.getStates().size());
        assertFalse(regionWithValidConditions.getStates().contains(state1));

        assertDoesNotThrow(() -> regionWithValidConditions.removeState(state2));
        assertTrue(regionWithValidConditions.getStates().isEmpty());

        Set<State> states = new HashSet<>();
        states.add(state1);
        states.add(state2);

        regionWithValidConditions.addStates(states);
        assertEquals(2, regionWithValidConditions.getStates().size());
        assertDoesNotThrow(() -> regionWithValidConditions.removeStates(states));
        assertTrue(regionWithValidConditions.getStates().isEmpty());

        assertThrows(NullPointerException.class, () -> regionWithValidConditions.addState(null));
        assertThrows(NullPointerException.class, () -> regionWithValidConditions.addStates(null));
        assertThrows(NullPointerException.class, () -> regionWithValidConditions.removeState(null));
        assertThrows(NullPointerException.class, () -> regionWithValidConditions.removeStates(null));
    }
}
