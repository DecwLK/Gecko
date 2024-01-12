package org.gecko.model;

import lombok.Getter;

@Getter
public class Edge implements Element {
    private Contract contract;
    private Kind kind;
    private int priority;

    public Edge(Contract contract, Kind kind, int priority) {
        //TODO stub
        this.contract = contract;
        this.kind = kind;
        this.priority = priority;
    }
    @Override
    public void accept(ElementVisitor visitor) {
        //TODO stub
    }
}
