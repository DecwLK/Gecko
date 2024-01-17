package org.gecko.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Edge extends Element {
    private Contract contract;
    private Kind kind;
    private int priority;
    private State source;
    private State destination;

    public Edge(int id, State source, State destination, Contract contract, Kind kind, int priority) {
        super(id);
        this.source = source;
        this.destination = destination;
        this.contract = contract;
        this.kind = kind;
        this.priority = priority;
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
