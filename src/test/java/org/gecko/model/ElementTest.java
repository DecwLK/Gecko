package org.gecko.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.gecko.exceptions.MissingViewModelElementException;
import org.gecko.exceptions.ModelException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ElementTest {
    static Element element;

    @BeforeAll
    static void setUp() {
        assertThrows(ModelException.class, () -> element = new Element(-1) {
            @Override
            public void accept(ElementVisitor visitor) throws ModelException, MissingViewModelElementException {

            }
        });
        assertDoesNotThrow(() -> element = new Element(0) {
            @Override
            public void accept(ElementVisitor visitor) throws ModelException, MissingViewModelElementException {

            }
        });
    }

    @Test
    void testHashCode() {
        assertNotEquals(0, element.hashCode());
    }

    @Test
    void testEquals() {
        assertTrue(element.equals(element));

        final Element[] other = new Element[4];
        assertDoesNotThrow(() -> other[0] = new Element(1) {
            @Override
            public void accept(ElementVisitor visitor) throws ModelException, MissingViewModelElementException {

            }
        });
        assertDoesNotThrow(() -> other[1] = new Element(0) {
            @Override
            public void accept(ElementVisitor visitor) throws ModelException, MissingViewModelElementException {

            }
        });

        assertDoesNotThrow(() -> other[2] = new State(2, "state"));
        assertDoesNotThrow(() -> other[3] = new State(0, "state"));

        assertFalse(element.equals(other[0]));
        assertTrue(element.equals(other[1]));
        assertFalse(element.equals(other[2]));
        assertTrue(element.equals(other[3]));
        assertFalse(element.equals(null));
    }
}
