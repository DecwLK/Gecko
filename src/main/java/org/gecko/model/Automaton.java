package org.gecko.model;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * Represents an automaton in the domain model of a Gecko project. An {@link Automaton} is described by a set of
 * {@link State}s, a set of {@link Edge}s that connect the states, a set of {@link Region}s that contain multiple states
 * and a start-{@link State}. Contains methods for managing the afferent data.
 */
@Data
public class Automaton {
    private State startState;
    private final Set<Region> regions;
    private final Set<State> states;
    private final Set<Edge> edges;

    @JsonCreator
    public Automaton() {
        this.startState = null;
        this.regions = new HashSet<>();
        this.states = new HashSet<>();
        this.edges = new HashSet<>();
    }

    public State getStateWithContract(Contract contract) {
        return states.stream().filter(state -> state.getContracts().contains(contract)).findFirst().orElse(null);
    }

    public void addRegion(Region region) {
        regions.add(region);
    }

    public void addRegions(Set<Region> regions) {
        this.regions.addAll(regions);
    }

    public void removeRegion(Region region) {
        regions.remove(region);
    }

    public void removeRegions(Set<Region> regions) {
        this.regions.removeAll(regions);
    }

    public void addState(State state) {
        states.add(state);
    }

    public void addStates(Set<State> states) {
        this.states.addAll(states);
    }

    public void removeState(State state) {
        states.remove(state);
    }

    public void removeStates(Set<State> states) {
        this.states.removeAll(states);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void addEdges(Set<Edge> edges) {
        this.edges.addAll(edges);
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }

    public void removeEdges(Set<Edge> edges) {
        this.edges.removeAll(edges);
    }

    @JsonIgnore
    public Set<Element> getAllElements() {
        Set<Element> allElements = new HashSet<>();
        allElements.addAll(regions);
        allElements.addAll(states);
        allElements.addAll(edges);
        return allElements;
    }
}
