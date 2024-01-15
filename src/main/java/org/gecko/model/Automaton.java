package org.gecko.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Automaton {
    private State startState;
    private final List<Region> regions;
    private final List<State> states;
    private final List<Edge> edges;

    public Automaton() {
        this.startState = null;
        this.regions = new ArrayList<>();
        this.states = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public State getStateWithContract(Contract contract) {
        return states.stream().filter(state -> state.getContracts().contains(contract)).findFirst().orElse(null);
    }

    public void addRegion(Region region) {
        regions.add(region);
    }

    public void addRegions(List<Region> regions) {
        this.regions.addAll(regions);
    }

    public void removeRegion(Region region) {
        regions.remove(region);
    }

    public void removeRegions(List<Region> regions) {
        this.regions.removeAll(regions);
    }

    public void addState(State state) {
        states.add(state);
    }

    public void addStates(List<State> states) {
        this.states.addAll(states);
    }

    public void removeState(State state) {
        states.remove(state);
    }

    public void removeStates(List<State> states) {
        this.states.removeAll(states);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void addEdges(List<Edge> edges) {
        this.edges.addAll(edges);
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }

    public void removeEdges(List<Edge> edges) {
        this.edges.removeAll(edges);
    }
}
