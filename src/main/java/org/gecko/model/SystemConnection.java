package org.gecko.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import org.gecko.exceptions.ModelException;

/**
 * Represents a system connection in the domain model of a Gecko project. A {@link SystemConnection} is described by a
 * source- and a destination-{@link Variable}.
 */
@Getter
public class SystemConnection extends Element {
    private Variable source;
    private Variable destination;

    @JsonCreator
    public SystemConnection(
        @JsonProperty("id") int id, @JsonProperty("source") Variable source,
        @JsonProperty("destination") Variable destination) {
        super(id);
        this.source = source;
        this.destination = destination;
    }

    public void setSource(@NonNull Variable source) throws ModelException {
        if (source.equals(destination)) {
            throw new ModelException("The source and the destination of a connection cannot be the same.");
        }
        this.source = source;
    }

    public void setDestination(@NonNull Variable destination) throws ModelException {
        if (this.destination.equals(destination)) {
            return;
        }
        if (destination.isHasIncomingConnection()) {
            throw new ModelException("The destination already has an incoming connection.");
        }
        if (destination.equals(source)) {
            throw new ModelException("The source and the destination of a connection cannot be the same.");
        }
        this.destination.setHasIncomingConnection(false);
        this.destination = destination;
        this.destination.setHasIncomingConnection(true);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
