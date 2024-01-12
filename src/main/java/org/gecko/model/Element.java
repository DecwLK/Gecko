package org.gecko.model;

public interface Element {
    public void accept(ElementVisitor visitor);
}
