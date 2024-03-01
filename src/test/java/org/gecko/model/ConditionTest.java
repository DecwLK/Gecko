package org.gecko.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.gecko.exceptions.ModelException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ConditionTest {
    static Condition condition;

    @BeforeAll
    static void setUp() {
        assertThrows(NullPointerException.class, () -> condition = new Condition(null));
        assertThrows(ModelException.class, () -> condition = new Condition(""));
        try {
            condition = new Condition("condition");
        } catch (ModelException e) {
            fail("Condition could not be initialized.");
        }
    }

    @Test
    void setCondition() {
        assertThrows(NullPointerException.class, () -> condition.setCondition(null));
        assertThrows(ModelException.class, () -> condition.setCondition(""));
        assertDoesNotThrow(() -> condition.setCondition("newCondition"));
    }

    @Test
    void and() {
        Condition other = null;
        try {
            other = new Condition("other");
        } catch (ModelException e) {
            fail("Support condition could not be initialized.");
        }

        assertNotNull(condition.and(other));
    }

    @Test
    void not() {
        assertNotNull(condition.not());
    }

    @Test
    void testToString() {
        assertEquals(condition.getCondition(), condition.toString());
    }
}
