package org.gecko.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a system connection in the domain model of a Gecko project. A {@link SystemConnection} is described by a
 * source- and a destination-{@link Variable}.
 */
@Getter
@Setter
public class SystemConnection extends Element {
    private Variable source;
    private Variable destination;

    @JsonCreator
    public SystemConnection(@JsonProperty("id") int id, @JsonProperty("source") Variable source, @JsonProperty("destination") Variable destination) {
        super(id);
        this.source = source;
        this.destination = destination;
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
