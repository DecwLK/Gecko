package org.gecko.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.exceptions.MissingViewModelElementException;
import org.gecko.exceptions.ModelException;

/**
 * Represents an edge in the domain model of a Gecko project. An {@link Edge} is described by a source- and a
 * destination-{@link State}. It is also associated with one of the start-{@link State}'s {@link Contract}s, has a
 * priority and a {@link Kind}, which informs about how the associated {@link Contract} is handled.
 */
@Getter
@Setter
public class Edge extends Element {
    private Contract contract;
    @Setter(onParam_ = {@NonNull})
    private Kind kind;
    private int priority;
    @Setter(onParam_ = {@NonNull})
    private State source;
    @Setter(onParam_ = {@NonNull})
    private State destination;

    @JsonCreator
    public Edge(
        @JsonProperty("id") int id, @JsonProperty("source") State source,
        @JsonProperty("destination") State destination, @JsonProperty("contract") Contract contract,
        @JsonProperty("kind") Kind kind, @JsonProperty("priority") int priority) throws ModelException {
        super(id);
        setSource(source);
        setDestination(destination);
        setContract(contract);
        setKind(kind);
        setPriority(priority);
    }

    public void setPriority(int priority) throws ModelException {
        if (priority < 0) {
            throw new ModelException("Negative edge priorities are not allowed.");
        }
        this.priority = priority;
    }

    @Override
    public void accept(ElementVisitor visitor) throws ModelException, MissingViewModelElementException {
        visitor.visit(this);
    }
}
