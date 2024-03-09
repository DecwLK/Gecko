package org.gecko.model;

import java.util.Objects;
import lombok.Getter;
import org.gecko.exceptions.MissingViewModelElementException;
import org.gecko.exceptions.ModelException;

/**
 * Represents an abstraction of an element in the domain model of a Gecko project. An {@link Element} has an id.
 */
@Getter
public abstract class Element {
    protected final int id;

    protected Element(int id) throws ModelException {
        if (id < 0) {
            throw new ModelException("Negative IDs are not allowed.");
        }
        this.id = id;
    }

    public abstract void accept(ElementVisitor visitor) throws ModelException, MissingViewModelElementException;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Element element)) {
            return false;
        }
        return id == element.id;
    }
}
