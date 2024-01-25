package org.gecko.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an edge in the domain model of a Gecko project. An {@link Edge} is described by a source- and a
 * destination-{@link State}. It is also associated with one of the start-{@link State}'s {@link Contract}s, has a
 * priority and a {@link Kind}, which informs about how the associated {@link Contract} is handled.
 */
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
