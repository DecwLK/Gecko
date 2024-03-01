package org.gecko.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.gecko.exceptions.ModelException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class VariableTest {
    static Variable variable;

    @BeforeAll
    static void setUp() {
        assertThrows(NullPointerException.class,
            () -> variable = new Variable(0, null, "int", Visibility.INPUT));
        assertThrows(NullPointerException.class,
            () -> variable = new Variable(0, "variable", null, Visibility.INPUT));


        assertThrows(ModelException.class, () -> variable = new Variable(0, "", null, Visibility.INPUT));
        assertThrows(ModelException.class,
            () -> variable = new Variable(0, "variable", "", Visibility.INPUT));

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
}
