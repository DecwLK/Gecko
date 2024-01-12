package org.gecko.model;

import lombok.Getter;

@Getter
public class Contract implements Renamable, Element {
    private Condition preCondition;
    private Condition postCondition;

    public Contract(Condition preCondition, Condition postCondition) {
        //TODO stub
        this.preCondition = preCondition;
        this.postCondition = postCondition;
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
