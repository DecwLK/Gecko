package org.gecko.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.gecko.exceptions.ModelException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EdgeTest {
    static Edge edge;
    static State source;
    static State destination;
    static Condition condition;
    static Contract contract;

    @BeforeAll
    static void setUp() {
        try {
            source = new State(0, "source");
            destination = new State(1, "destination");
            condition = new Condition("true");
            contract = new Contract(2, "contract", condition, condition);
        } catch (ModelException e) {
            fail("States or contract for testing purposes of the edge could not be initialized.");
        }

        assertThrows(ModelException.class, () -> edge = new Edge(2, source, destination, contract, Kind.HIT, -5));

        try {
            edge = new Edge(2, source, destination, contract, Kind.HIT, 0);
        } catch (ModelException e) {
            fail("Edge could not be initialized.");
        }
    }

    @Test
    void testInitializedEdge() {
        assertNotNull(edge);
        assertNotNull(edge.getSource());
        assertNotNull(edge.getDestination());
        assertNotNull(edge.getContract());
        assertNotNull(edge.getKind());
    }

    @Test
    void setPriority() {
        assertThrows(ModelException.class, () -> edge.setPriority(-1));
        assertDoesNotThrow(() -> edge.setPriority(3));
    }

    @Test
    void setContractToNull() {
        edge.setContract(null);
        assertNull(edge.getContract());
    }

    @Test
    void testNullParametersInEdge() {
        Edge edge = null;
        try {
            edge = new Edge(0, new State(1, "source"), new State(2, "destination"),
                new Contract(3, "contract", new Condition("true"), new Condition("true")), Kind.HIT, 0);
        } catch (ModelException e) {
            fail("Failed to create edge for testing purposes of its setters.");
        }

        try {
            edge.setSource(null);
        } catch (NullPointerException e) {
            assertNotNull(edge.getSource());
        }

        try {
            edge.setDestination(null);
        } catch (NullPointerException e) {
            assertNotNull(edge.getDestination());
        }

        try {
            edge.setKind(null);
        } catch (NullPointerException e) {
            assertNotNull(edge.getKind());
        }

        assertNotNull(edge.getSource());
        assertNotNull(edge.getDestination());
        assertNotNull(edge.getKind());
    }
}
