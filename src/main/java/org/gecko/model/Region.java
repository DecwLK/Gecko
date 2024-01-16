package org.gecko.model;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class Region implements Renamable, Element {
    private String name;
    private Condition invariant;
    private Contract preAndPostCondition;
    private final Set<State> states;

    public Region(String name, Condition invariant, Contract preAndPostCondition) {
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
