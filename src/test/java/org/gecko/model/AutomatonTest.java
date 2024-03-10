package org.gecko.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;
import java.util.Set;
import org.gecko.exceptions.ModelException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AutomatonTest {
    static Automaton defaultAutomaton;
    static Automaton automatonWithStartState;
    static Contract contract;
    static State startState;
    static State ordinaryState;
    static Region region1;
    static Region region2;
    static Edge edge1;
    static Edge edge2;


    @BeforeAll
    static void setUp() {
        defaultAutomaton = new Automaton();
        contract = null;
        Condition condition = null;

        try {
            condition = new Condition("true");
            contract = new Contract(2, "contract", condition, condition);
        } catch (ModelException e) {
            fail("Condition and contract for testing purposes of the automaton could not be initialized.");
        }

        try {
            startState = new State(0, "startState");
            ordinaryState = new State(1, "ordinaryState");
        } catch (ModelException e) {
            fail("States for testing purposes of the automaton could not be initialized.");
        }
        automatonWithStartState = new Automaton();
        automatonWithStartState.addState(startState);
        assertDoesNotThrow(() -> automatonWithStartState.setStartState(startState));
        assertNotNull(automatonWithStartState.getStartState());

        try {
            region1 = new Region(3, "region1", condition, contract);
            region2 = new Region(4, "region2", condition, contract);
        } catch (ModelException e) {
            fail("Regions for testing purposes of the automaton could not be initialized.");
        }

        try {
            edge1 = new Edge(5, startState, ordinaryState, contract, Kind.HIT, 0);
            edge2 = new Edge(6, startState, ordinaryState, contract, Kind.MISS, 1);
        } catch (ModelException e) {
            fail("Edges for testing purposes of the automaton could not be initialized.");
        }
    }

    @Test
    void testInitializedDefaultAutomaton() {
        assertNull(defaultAutomaton.getStartState());
        assertNotNull(defaultAutomaton.getRegions());
        assertNotNull(defaultAutomaton.getStates());
        assertNotNull(defaultAutomaton.getEdges());
    }

    @Test
    void testInitializedAutomatonWithStartState() {
        assertNotNull(automatonWithStartState.getStartState());
        assertNotNull(automatonWithStartState.getRegions());
        assertNotNull(automatonWithStartState.getStates());
        assertNotNull(automatonWithStartState.getEdges());
    }

    @Test
    void testManagingRegions() {
        assertTrue(automatonWithStartState.getRegions().isEmpty());

        automatonWithStartState.addRegion(region1);
        assertFalse(automatonWithStartState.getRegions().isEmpty());

        automatonWithStartState.addRegion(region2);
        assertEquals(2, automatonWithStartState.getRegions().size());

        automatonWithStartState.removeRegion(region1);
        assertEquals(1, automatonWithStartState.getRegions().size());
        assertFalse(automatonWithStartState.getRegions().contains(region1));

        automatonWithStartState.removeRegion(region2);
        assertTrue(automatonWithStartState.getRegions().isEmpty());

        Set<Region> regions = new HashSet<>();
        regions.add(region1);
        regions.add(region2);

        automatonWithStartState.addRegions(regions);
        assertEquals(2, automatonWithStartState.getRegions().size());
        automatonWithStartState.removeRegions(regions);
        assertTrue(automatonWithStartState.getRegions().isEmpty());

        assertThrows(NullPointerException.class, () -> automatonWithStartState.addRegion(null));
        assertThrows(NullPointerException.class, () -> automatonWithStartState.addRegions(null));
        assertThrows(NullPointerException.class, () -> automatonWithStartState.removeRegion(null));
        assertThrows(NullPointerException.class, () -> automatonWithStartState.removeRegions(null));
    }

    @Test
    void getRegionsWithState() {
        region2.addState(ordinaryState);
        region2.addState(startState);

        defaultAutomaton.addRegion(region1);
        defaultAutomaton.addRegion(region2);

        assertEquals(1, defaultAutomaton.getRegionsWithState(startState).size());
        assertEquals(region2, defaultAutomaton.getRegionsWithState(startState).getFirst());

        region2.removeState(startState);
        assertTrue(defaultAutomaton.getRegionsWithState(startState).isEmpty());

        region2.removeState(ordinaryState);
        defaultAutomaton.removeRegion(region1);
        defaultAutomaton.removeRegion(region2);
    }

    @Test
    void testManagingStartState() {
        assertThrows(ModelException.class, () -> defaultAutomaton.setStartState(startState));

        defaultAutomaton.addState(startState);
        assertDoesNotThrow(() -> defaultAutomaton.setStartState(startState));
        assertNotNull(defaultAutomaton.getStartState());

        defaultAutomaton.addState(ordinaryState);
        assertThrows(ModelException.class, () -> defaultAutomaton.removeState(startState));

        assertDoesNotThrow(() -> defaultAutomaton.removeState(ordinaryState));
        assertDoesNotThrow(() -> defaultAutomaton.removeState(startState));
        assertNull(defaultAutomaton.getStartState());
    }

    @Test
    void testManagingStates() {
        assertDoesNotThrow(() -> automatonWithStartState.removeState(startState));
        automatonWithStartState.addState(startState);
        assertDoesNotThrow(() -> automatonWithStartState.setStartState(startState));

        assertTrue(defaultAutomaton.getStates().isEmpty());

        defaultAutomaton.addState(startState);
        assertFalse(defaultAutomaton.getStates().isEmpty());

        defaultAutomaton.addState(ordinaryState);
        assertEquals(2, defaultAutomaton.getStates().size());

        assertDoesNotThrow(() -> defaultAutomaton.removeState(startState));
        assertEquals(1, defaultAutomaton.getStates().size());
        assertFalse(defaultAutomaton.getStates().contains(startState));
        assertDoesNotThrow(() -> defaultAutomaton.removeState(startState));

        assertDoesNotThrow(() -> defaultAutomaton.removeState(ordinaryState));
        assertTrue(defaultAutomaton.getStates().isEmpty());

        Set<State> states = new HashSet<>();
        states.add(startState);
        states.add(ordinaryState);

        defaultAutomaton.addStates(states);
        assertEquals(2, defaultAutomaton.getStates().size());
        assertDoesNotThrow(() -> defaultAutomaton.removeStates(states));
        assertTrue(defaultAutomaton.getStates().isEmpty());

        assertThrows(NullPointerException.class, () -> defaultAutomaton.addState(null));
        assertThrows(NullPointerException.class, () -> defaultAutomaton.addStates(null));
        assertThrows(NullPointerException.class, () -> defaultAutomaton.removeState(null));
        assertThrows(NullPointerException.class, () -> defaultAutomaton.removeStates(null));
    }

    @Test
    void getStateWithContract() {
        ordinaryState.addContract(contract);
        defaultAutomaton.addState(startState);
        defaultAutomaton.addState(ordinaryState);

        assertEquals(ordinaryState, defaultAutomaton.getStateWithContract(contract));
        assertDoesNotThrow(() -> defaultAutomaton.removeState(ordinaryState));
        assertNull(defaultAutomaton.getStateWithContract(contract));
        assertDoesNotThrow(() -> defaultAutomaton.removeState(startState));
    }

    @Test
    void getStateByName() {
        defaultAutomaton.addState(startState);
        defaultAutomaton.addState(ordinaryState);

        assertEquals(ordinaryState, defaultAutomaton.getStateByName(ordinaryState.getName()));
        assertDoesNotThrow(() -> defaultAutomaton.removeState(ordinaryState));
        assertNull(defaultAutomaton.getStateByName(ordinaryState.getName()));
        assertDoesNotThrow(() -> defaultAutomaton.removeState(startState));
    }

    @Test
    void testManagingEdges() {
        assertTrue(defaultAutomaton.getEdges().isEmpty());

        defaultAutomaton.addEdge(edge1);
        assertFalse(defaultAutomaton.getEdges().isEmpty());

        defaultAutomaton.addEdge(edge2);
        assertEquals(2, defaultAutomaton.getEdges().size());

        defaultAutomaton.removeEdge(edge1);
        assertEquals(1, defaultAutomaton.getEdges().size());
        assertFalse(defaultAutomaton.getEdges().contains(edge1));

        defaultAutomaton.removeEdge(edge2);
        assertTrue(defaultAutomaton.getEdges().isEmpty());

        Set<Edge> edges = new HashSet<>();
        edges.add(edge1);
        edges.add(edge2);

        defaultAutomaton.addEdges(edges);
        assertEquals(2, defaultAutomaton.getEdges().size());
        assertDoesNotThrow(() -> defaultAutomaton.removeEdges(edges));
        assertTrue(defaultAutomaton.getEdges().isEmpty());

        assertThrows(NullPointerException.class, () -> defaultAutomaton.addEdge(null));
        assertThrows(NullPointerException.class, () -> defaultAutomaton.addEdges(null));
        assertThrows(NullPointerException.class, () -> defaultAutomaton.removeEdge(null));
        assertThrows(NullPointerException.class, () -> defaultAutomaton.removeEdges(null));
    }

    @Test
    void getOutgoingEdges() {
        defaultAutomaton.addEdge(edge1);
        defaultAutomaton.addEdge(edge2);
        assertEquals(2, defaultAutomaton.getOutgoingEdges(startState).size());

        defaultAutomaton.removeEdge(edge2);
        assertEquals(1, defaultAutomaton.getOutgoingEdges(startState).size());

        defaultAutomaton.removeEdge(edge1);
        assertTrue(defaultAutomaton.getOutgoingEdges(startState).isEmpty());
    }

    @Test
    void isEmpty() {
        assertTrue(defaultAutomaton.isEmpty());
        assertFalse(automatonWithStartState.isEmpty());

        defaultAutomaton.addState(startState);
        assertFalse(defaultAutomaton.isEmpty());
        assertDoesNotThrow(() -> defaultAutomaton.removeState(startState));

        defaultAutomaton.addEdge(edge1);
        assertFalse(defaultAutomaton.isEmpty());
        assertDoesNotThrow(() -> defaultAutomaton.removeEdge(edge1));

        defaultAutomaton.addRegion(region1);
        assertFalse(defaultAutomaton.isEmpty());
        assertDoesNotThrow(() -> defaultAutomaton.removeRegion(region1));
    }

    @Test
    void getAllElements() {
        assertTrue(defaultAutomaton.getAllElements().isEmpty());
        assertFalse(automatonWithStartState.getAllElements().isEmpty());
    }
}