package org.gecko.model;

import java.util.Objects;
import lombok.Getter;

/**
 * Represents an abstraction of an element in the domain model of a Gecko project. An {@link Element} has an id and accepts {@link ElementVisitor}s.
 */
@Getter
public abstract class Element {
    protected final int id;

    protected Element(int id) {
        this.id = id;
    }

    public abstract void accept(ElementVisitor visitor);

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Element element = (Element) o;
        return id == element.id;
    }
}
