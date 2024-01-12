package org.gecko.model;

import lombok.Getter;

@Getter
public class Contract {
    private Condition preCondition;
    private Condition postCondition;

    public Contract(Condition preCondition, Condition postCondition) {
        //TODO stub
        this.preCondition = preCondition;
        this.postCondition = postCondition;
    }
}
