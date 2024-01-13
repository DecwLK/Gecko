package org.gecko.model;

import lombok.Getter;

import java.util.List;

public class System implements Renamable, Element {
    @Getter private System parent;
    @Getter private String code;
    @Getter private Automaton automaton;
    @Getter private List<System> children;
    private List<SystemConnection> connections;
    private List<Variable> variables;

    public void addChild(System child) {
        // TODO stub
        children.add(child);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        // TODO stub
    }

    @Override
    public String getName() {
        // TODO stub
        return null;
    }

    @Override
    public void setName(String name) {
        // TODO stub
    }
}
