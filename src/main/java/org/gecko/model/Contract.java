package org.gecko.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.gecko.exceptions.ModelException;

/**
 * Represents a contract in the domain model of a Gecko project. A {@link Contract} has a name and is described by a
 * pre- and a post-{@link Condition}.
 */

@Getter
public class Contract extends Element implements Renamable {
    private String name;
    private Condition preCondition;
    private Condition postCondition;

    @JsonCreator
    public Contract(
        @JsonProperty("id") int id, @JsonProperty("name") String name,
        @JsonProperty("preCondition") Condition preCondition, @JsonProperty("postCondition") Condition postCondition)
        throws ModelException {
        super(id);
        setName(name);
        setPreCondition(preCondition);
        setPostCondition(postCondition);
    }

    public void setName(String name) throws ModelException {
        if (name == null || name.isEmpty()) {
            throw new ModelException("Contract's name is invalid.");
        }
        this.name = name;
    }

    public void setPreCondition(Condition preCondition) throws ModelException {
        if (preCondition == null) {
            throw new ModelException("Precondition for contract is null.");
        }
        this.preCondition = preCondition;
    }

    public void setPostCondition(Condition postCondition) throws ModelException {
        if (postCondition == null) {
            throw new ModelException("Postcondition for contract is null.");
        }
        this.postCondition = postCondition;
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
