package org.gecko.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.gecko.exceptions.ModelException;

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
    private boolean hasIncomingConnection;

    @JsonCreator
    public Variable(
        @JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("type") String type,
        @JsonProperty("visibility") Visibility visibility) throws ModelException {
        super(id);
        setVisibility(visibility);
        setName(name);
        setType(type);
    }

    public void setName(String name) throws ModelException {
        if (name == null || name.isEmpty()) {
            throw new ModelException("Variable's name is invalid.");
        }
        this.name = name;
    }

    public void setVisibility(Visibility visibility) throws ModelException {
        if (visibility == null) {
            throw new ModelException("Visibility is null.");
        }
        this.visibility = visibility;
    }

    public void setType(String type) throws ModelException {
        if (type == null || type.isEmpty()) {
            throw new ModelException("Variable's type is invalid.");
        }
        this.type = type;
    }

    @Override
    public void accept(ElementVisitor visitor) throws ModelException {
        visitor.visit(this);
    }
}
