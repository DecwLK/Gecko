package org.gecko.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.exceptions.ModelException;

/**
 * Represents an automaton in the domain model of a Gecko project. An {@link Automaton} is described by a set of
 * {@link State}s, a set of {@link Edge}s that connect the states, a set of {@link Region}s that contain multiple states
 * and a start-{@link State}. Contains methods for managing the afferent data.
 */
@Data
@Setter(AccessLevel.NONE)
public class Automaton {
    private State startState;
    private final Set<Region> regions;
    private final Set<State> states;
    private final Set<Edge> edges;

    @JsonCreator
    public Automaton() {
        this.regions = new HashSet<>();
        this.states = new HashSet<>();
        this.edges = new HashSet<>();
    }

    public Automaton(@JsonProperty("startState") State startState) {
        this.regions = new HashSet<>();
        this.states = new HashSet<>();
        this.edges = new HashSet<>();
        this.startState = startState;
    }

    public void setStartState(State state) throws ModelException {
        if (state != null && !states.contains(state)) {
            throw new ModelException("State cannot be set as start-state.");
        }
        startState = state;
    }

    @JsonIgnore
    public State getStateWithContract(Contract contract) {
        return states.stream().filter(state -> state.getContracts().contains(contract)).findFirst().orElse(null);
    }

    public void addRegion(@NonNull Region region) {
        regions.add(region);
    }

    public void addRegions(@NonNull Set<Region> regions) {
        for (Region region : regions) {
            addRegion(region);
        }
    }

    public void removeRegion(@NonNull Region region) {
        regions.remove(region);
    }

    public void removeRegions(@NonNull Set<Region> regions) {
        for (Region region : regions) {
            removeRegion(region);
        }
    }

    public void addState(@NonNull State state) {
        states.add(state);
    }

    public void addStates(@NonNull Set<State> states) {
        for (State state : states) {
            addState(state);
        }
    }

    public void removeState(@NonNull State state) throws ModelException {
        if (states.size() == 1 && states.contains(state)) {
            setStartState(null);
            states.remove(state);
            return;
        }
        if (state.equals(startState) && states.size() > 1) {
            throw new ModelException("Cannot remove the start state of an automaton.");
        }
        states.remove(state);
    }

    public void removeStates(@NonNull Set<State> states) throws ModelException {
        for (State state : states) {
            removeState(state);
        }
    }

    public void addEdge(@NonNull Edge edge) {
        edges.add(edge);
    }

    public void addEdges(@NonNull Set<Edge> edges) {
        for (Edge edge : edges) {
            addEdge(edge);
        }
    }

    public void removeEdge(@NonNull Edge edge) {
        edges.remove(edge);
    }

    public void removeEdges(@NonNull Set<Edge> edges) {
        for (Edge edge : edges) {
            removeEdge(edge);
        }
    }

    @JsonIgnore
    public State getStateByName(String name) {
        return states.stream().filter(state -> state.getName().equals(name)).findFirst().orElse(null);
    }

    @JsonIgnore
    public List<Edge> getOutgoingEdges(State state) {
        return edges.stream().filter(edge -> edge.getSource().equals(state)).toList();
    }

    @JsonIgnore
    public List<Region> getRegionsWithState(State state) {
        return regions.stream().filter(region -> region.getStates().contains(state)).toList();
    }

    @JsonIgnore
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
