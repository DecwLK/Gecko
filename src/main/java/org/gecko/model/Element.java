package org.gecko.model;

public interface Element {
    void accept(ElementVisitor visitor);
}
