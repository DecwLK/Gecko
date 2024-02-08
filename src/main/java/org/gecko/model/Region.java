package org.gecko.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.gecko.exceptions.ModelException;

/**
 * Represents a region in the domain model of a Gecko project. A {@link Region} has a name and is described by a set of
 * {@link State}s, a {@link Contract} and an invariant-{@link Condition}. Contains methods for managing the afferent
 * data.
 */
@Setter
@Getter
public class Region extends Element implements Renamable {
    private String name;
    private final Condition invariant;
    private final Contract preAndPostCondition;
    private final Set<State> states;

    @JsonCreator
    public Region(
        @JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("invariant") Condition invariant,
        @JsonProperty("preAndPostCondition") Contract preAndPostCondition) throws ModelException {
        super(id);
        setName(name);
        this.states = new HashSet<>();
        this.invariant = invariant;
        this.preAndPostCondition = preAndPostCondition;
    }

    public void setName(String name) throws ModelException {
        if (name == null || name.isEmpty()) {
            throw new ModelException("Region's name is invalid.");
        }
        this.name = name;
    }

    public void setPreCondition(Condition preCondition) throws ModelException {
        if (preCondition == null) {
            throw new ModelException("Region's name is invalid.");
        }
        this.preAndPostCondition.setPreCondition(preCondition);
    }

    public void setPostCondition(Condition postCondition) throws ModelException {
        if (postCondition == null) {
            throw new ModelException("Region's name is invalid.");
        }
        this.preAndPostCondition.setPreCondition(postCondition);
    }

    @Override
    public void accept(ElementVisitor visitor) throws MatchException, ModelException {
        visitor.visit(this);
    }

    public void addState(State state) throws ModelException {
        if (state == null || states.contains(state)) {
            throw new ModelException("Cannot add state to region.");
        }
        states.add(state);
    }

    public void addStates(Set<State> states) throws ModelException {
        for (State state : states) {
            addState(state);
        }
    }

    public void removeState(State state) throws ModelException {
        if (state == null || !states.contains(state)) {
            throw new ModelException("Cannot remove state from region.");
        }
        states.remove(state);
    }

    public void removeStates(Set<State> states) throws ModelException {
        for (State state : states) {
            removeState(state);
        }
    }
}
