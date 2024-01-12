package org.gecko.model;

import lombok.Getter;

import java.util.List;

@Getter
public class State implements Renamable, Element {
    private List<Contract> contracts;

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
