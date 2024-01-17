package org.gecko.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a variable in the domain model of a Gecko project.
 * A {@link Variable} has a name, a type and a {@link Visibility}.
 */
@Getter
@Setter
public class Variable extends Element implements Renamable {
    private String name;
    private String type;
    private Visibility visibility;

    public Variable(int id, String name, String type, Visibility visibility) {
        super(id);
        this.visibility = visibility;
        this.name = name;
        this.type = type;
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
