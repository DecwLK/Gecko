package org.gecko.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import org.gecko.exceptions.ModelException;

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
    public Automaton() throws ModelException {
        setStartState(null);
        this.regions = new HashSet<>();
        this.states = new HashSet<>();
        this.edges = new HashSet<>();
    }

    public void setStartState(State state) throws ModelException {
        if (state != null && !states.contains(state)) {
            throw new ModelException("State cannot be set as start-state.");
        }
    }

    public State getStateWithContract(Contract contract) {
        return states.stream().filter(state -> state.getContracts().contains(contract)).findFirst().orElse(null);
    }

    public void addRegion(Region region) throws ModelException {
        if (region == null || regions.contains(region)) {
            throw new ModelException("Cannot add region to automaton.");
        }
        regions.add(region);
    }

    public void addRegions(Set<Region> regions) throws ModelException {
        for (Region region : regions) {
            addRegion(region);
        }
    }

    public void removeRegion(Region region) throws ModelException {
        if (region == null || !regions.contains(region)) {
            throw new ModelException("Cannot remove region from automaton.");
        }
        regions.remove(region);
    }

    public void removeRegions(Set<Region> regions) throws ModelException {
        for (Region region : regions) {
            removeRegion(region);
        }
    }

    public void addState(State state) throws ModelException {
        if (state == null || states.contains(state)) {
            throw new ModelException("Cannot add state to automaton.");
        }
        states.add(state);
    }

    public void addStates(Set<State> states) throws ModelException {
        for (State state : states) {
            addState(state);
        }
    }

    public void removeState(State state) throws ModelException {
        if (states.size() <= 1 && states.contains(state)) {
            setStartState(null);
            states.remove(state);
            return;
        }
        if (state.equals(startState) && states.size() > 1) {
            throw new ModelException("Cannot remove the start state of an automaton.");
        }
        states.remove(state);
    }

    public void removeStates(Set<State> states) throws ModelException {
        for (State state : states) {
            removeState(state);
        }
    }

    public void addEdge(Edge edge) throws ModelException {
        if (edge == null || edges.contains(edge)) {
            throw new ModelException("Cannot add edge to automaton.");
        }
        edges.add(edge);
    }

    public void addEdges(Set<Edge> edges) throws ModelException {
        for (Edge edge : edges) {
            addEdge(edge);
        }
    }

    public void removeEdge(Edge edge) throws ModelException {
        if (edge == null || !edges.contains(edge)) {
            throw new ModelException("Cannot remove edge to automaton.");
        }
        edges.remove(edge);
    }

    public void removeEdges(Set<Edge> edges) throws ModelException {
        for (Edge edge : edges) {
            removeEdge(edge);
        }
    }

    public State getStateByName(String name) {
        return states.stream().filter(state -> state.getName().equals(name)).findFirst().orElse(null);
    }

    public List<Edge> getOutgoingEdges(State state) {
        return edges.stream().filter(edge -> edge.getSource().equals(state)).collect(Collectors.toList());
    }

    public List<Region> getRegionsWithState(State state) {
        return regions.stream().filter(region -> region.getStates().contains(state)).collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return states.isEmpty() && edges.isEmpty() && regions.isEmpty();
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
