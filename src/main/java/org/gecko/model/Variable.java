package org.gecko.model;

import lombok.Data;

@Data
public class Variable implements Renamable, Element {
    private String name;
    private String type;
    private Visibility visibility;

    public Variable(String name, String type, Visibility visibility) {
        this.visibility = visibility;
        this.name = name;
        this.type = type;
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
