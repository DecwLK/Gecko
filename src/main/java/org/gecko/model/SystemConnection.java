package org.gecko.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemConnection extends Element {
    private Variable source;
    private Variable destination;

    public SystemConnection(int id, Variable source, Variable destination) {
        super(id);
        this.source = source;
        this.destination = destination;
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
