package org.gecko.model;

import lombok.Getter;

@Getter
public class Variable implements Renamable, Element {
    private Visibility visibility;
    private String name;
    private String type;

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
