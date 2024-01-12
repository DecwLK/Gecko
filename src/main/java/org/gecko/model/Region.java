package org.gecko.model;

import lombok.Getter;

import java.util.List;

public class Region implements Renamable, Element {
    @Getter
    private Condition invariant;
    private Contract preAndPostCondition;
    @Getter
    private List<State> states;

    public Region(Condition invariant, Contract preAndPostCondition) {
        this.invariant = invariant;
        this.preAndPostCondition = preAndPostCondition;
    }
    @Override
    public void accept(ElementVisitor visitor) {
        //TODO stub
    }

    @Override
    public String getName() {
        //TODO stub
        return null;
    }

    @Override
    public void setName(String name) {
        //TODO stub
    }
}
