package org.gecko.model;

import static org.gecko.model.GeckoModelTest.NULL_PARAMETERS_FAIL;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.gecko.exceptions.ModelException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class VariableTest {
    static Variable variable;

    @BeforeAll
    static void setUp() {
        assertThrows(NullPointerException.class, () -> variable = new Variable(0, null, "int", Visibility.INPUT));
        assertThrows(NullPointerException.class, () -> variable = new Variable(0, "variable", null, Visibility.INPUT));


        assertThrows(ModelException.class, () -> variable = new Variable(0, "", null, Visibility.INPUT));
        assertThrows(ModelException.class, () -> variable = new Variable(0, "variable", "", Visibility.INPUT));

        assertDoesNotThrow(() -> variable = new Variable(0, "variable", "int", Visibility.INPUT));
    }

    @Test
    void setName() {
        assertThrows(NullPointerException.class, () -> variable.setName(null));
        assertThrows(ModelException.class, () -> variable.setName(""));
        assertDoesNotThrow(() -> variable.setName("newName"));
        assertEquals("newName", variable.getName());
    }

    @Test
    void setType() {
        assertThrows(NullPointerException.class, () -> variable.setType(null));
        assertThrows(ModelException.class, () -> variable.setType(""));
        assertDoesNotThrow(() -> variable.setType("long"));
        assertEquals("long", variable.getType());
    }

    @Test
    void setValue() {
        assertThrows(ModelException.class, () -> variable.setValue(""));
        assertDoesNotThrow(() -> variable.setValue(null));
        assertNull(variable.getValue());
        assertDoesNotThrow(() -> variable.setValue("value"));
        assertEquals("value", variable.getValue());
    }

    @Test
    void getBuiltinTypes() {
        assertEquals(15, Variable.getBuiltinTypes().size());
    }

    @Test
    void testNullParametersInVariable() {
        Variable variable = null;
        try {
            variable = new Variable(0, "variable", "bool", Visibility.INPUT);
        } catch (ModelException e) {
            fail("Failed to create variable for testing purposes of a its setters.");
        }

        try {
            variable.setName(null);
        } catch (NullPointerException e) {
            assertNotNull(variable.getName());
        } catch (ModelException e) {
            fail(NULL_PARAMETERS_FAIL);
        }

        try {
            variable.setType(null);
        } catch (NullPointerException e) {
            assertNotNull(variable.getType());
        } catch (ModelException e) {
            fail(NULL_PARAMETERS_FAIL);
        }

        try {
            variable.setVisibility(null);
        } catch (NullPointerException e) {
            assertNotNull(variable.getVisibility());
        }

        assertNotNull(variable.getName());
        assertNotNull(variable.getType());
        assertNotNull(variable.getVisibility());
    }
}
