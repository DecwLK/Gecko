package org.gecko.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

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
    public Region(@JsonProperty("id") int id, @JsonProperty("name") String name,
                  @JsonProperty("invariant") Condition invariant,
                  @JsonProperty("preAndPostCondition") Contract preAndPostCondition) {
        super(id);
        this.name = name;
        this.invariant = invariant;
        this.preAndPostCondition = preAndPostCondition;
        this.states = new HashSet<>();
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
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
}
