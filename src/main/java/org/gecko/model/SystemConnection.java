package org.gecko.model;

import lombok.Data;

@Data
public class SystemConnection implements Element {
    private Variable source;
    private Variable destination;

    public SystemConnection(Variable source, Variable destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
