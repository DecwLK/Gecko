package org.gecko.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.gecko.exceptions.ModelException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SystemConnectionTest {
    static SystemConnection connection;
    static Variable source1;
    static Variable source2;
    static Variable destination1;
    static Variable destination2;

    @BeforeAll
    static void setUp() {
        assertDoesNotThrow(() -> source1 = new Variable(0, "source1", "type", Visibility.OUTPUT));
        assertDoesNotThrow(() -> source2 = new Variable(1, "source2", "type", Visibility.OUTPUT));
        assertDoesNotThrow(() -> destination1 = new Variable(2, "destination1", "type", Visibility.INPUT));
        assertDoesNotThrow(() -> destination2 = new Variable(3, "destination2", "type", Visibility.INPUT));

        assertThrows(NullPointerException.class, () -> connection = new SystemConnection(4, null, destination1));
        assertThrows(NullPointerException.class, () -> connection = new SystemConnection(4, source1, null));

        assertDoesNotThrow(() -> connection = new SystemConnection(4, source1, destination1));
    }

    @Test
    void setSource() {
        assertNotNull(connection.getSource());
        assertThrows(ModelException.class, () -> connection.setSource(destination1));
        assertDoesNotThrow(() -> connection.setSource(source2));
        assertNotNull(connection.getSource());
        assertDoesNotThrow(() -> connection.setSource(source1));
    }

    @Test
    void setDestination() {
        assertNotNull(connection.getDestination());
        assertDoesNotThrow(() -> connection.setDestination(destination1));
        assertThrows(ModelException.class, () -> connection.setDestination(source1));
        assertDoesNotThrow(() -> connection.setDestination(destination2));
        assertNotNull(connection.getDestination());
        assertDoesNotThrow(() -> connection.setDestination(destination1));

        try {
            new SystemConnection(5, source2, destination2);
        } catch (ModelException e) {
            fail("Could not initialize connection for testing the behaviour of another connection.");
        }
        assertThrows(ModelException.class, () -> connection.setDestination(destination2));
        assertDoesNotThrow(() -> connection.setDestination(destination1));

    }
}