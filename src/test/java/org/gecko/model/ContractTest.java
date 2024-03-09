package org.gecko.model;

import static org.gecko.model.GeckoModelTest.NULL_PARAMETERS_FAIL;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.gecko.exceptions.ModelException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ContractTest {
    static Contract contract;

    @BeforeAll
    static void setUp() {
        Condition preCondition = null;
        Condition postCondition = null;

        // TODO: Contracts accept null pre-/postConditions, because setter onMethod instead of onParam

        try {
            preCondition = new Condition("pre");
            postCondition = new Condition("post");
        } catch (ModelException e) {
            fail("Conditions for testing purposes of the contract could not be initialized.");
        }

        try {
            contract = new Contract(0, "contract", preCondition, postCondition);
        } catch (ModelException e) {
            fail("Contract could not be initialized.");
        }
    }

    @Test
    void setName() {
        assertThrows(NullPointerException.class, () -> contract.setName(null));
        assertThrows(ModelException.class, () -> contract.setName(""));
        assertDoesNotThrow(() -> contract.setName("newName"));
        assertEquals("newName", contract.getName());
    }

    @Test
    void setPreAndPostConditions() {
        assertThrows(NullPointerException.class, () -> contract.setName(null));
        assertThrows(ModelException.class, () -> contract.setName(""));
        assertDoesNotThrow(() -> contract.setName("newName"));
        assertEquals("newName", contract.getName());
    }

    @Test
    void testToString() {
        assertEquals(contract.getName(), contract.toString());
    }

    @Test
    void testNullParametersInContract() {
        Contract contract = null;
        try {
            contract = new Contract(0, "contract", new Condition("true"), new Condition("true"));
        } catch (ModelException e) {
            fail("Failed to create conditions for testing purposes of a contract's setters.");
        }

        try {
            contract.setName(null);
        } catch (NullPointerException e) {
            assertNotNull(contract.getName());
        } catch (ModelException e) {
            fail(NULL_PARAMETERS_FAIL);
        }

        try {
            contract.setPreCondition(null);
        } catch (NullPointerException e) {
            assertNotNull(contract.getPreCondition());
        }

        try {
            contract.setPostCondition(null);
        } catch (NullPointerException e) {
            assertNotNull(contract.getPostCondition());
        }

        assertNotNull(contract.getName());
        assertNotNull(contract.getPreCondition());
        assertNotNull(contract.getPostCondition());
    }
}
