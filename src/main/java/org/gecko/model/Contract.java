package org.gecko.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contract extends Element implements Renamable {
    private String name;
    private Condition preCondition;
    private Condition postCondition;

    public Contract(int id, String name, Condition preCondition, Condition postCondition) {
        super(id);
        this.name = name;
        this.preCondition = preCondition;
        this.postCondition = postCondition;
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
