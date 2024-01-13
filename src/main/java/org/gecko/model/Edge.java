package org.gecko.model;

import lombok.Data;

@Data
public class Edge implements Element {
    private Contract contract;
    private Kind kind;
    private int priority;
    private State source;
    private State target;

    public Edge(State source, State target, Contract contract, Kind kind, int priority) {
        this.contract = contract;
        this.kind = kind;
        this.priority = priority;
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
