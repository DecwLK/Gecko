package org.gecko.model;

import lombok.Data;

@Data
public class Contract implements Renamable, Element {
    private String name;
    private Condition preCondition;
    private Condition postCondition;

    public Contract(String name, Condition preCondition, Condition postCondition) {
        this.name = name;
        this.preCondition = preCondition;
        this.postCondition = postCondition;
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
