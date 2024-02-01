package org.gecko.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a variable in the domain model of a Gecko project. A {@link Variable} has a name, a type and a
 * {@link Visibility}.
 */
@Getter
@Setter
public class Variable extends Element implements Renamable {
    private String name;
    private String type;
    private Visibility visibility;

    @JsonCreator
    public Variable(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("type") String type,
                    @JsonProperty("visibility") Visibility visibility) {
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
