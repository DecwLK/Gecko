package org.gecko.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.gecko.exceptions.ModelException;

/**
 * Represents a condition in the domain model of a Gecko project.
 */
@Data
public class Condition {
    private String condition;

    @JsonCreator
    public Condition(@JsonProperty("condition") String condition) throws ModelException {
        setCondition(condition);
    }

    public void setCondition(String condition) throws ModelException {
        if (condition == null || condition.isEmpty()) {
            throw new ModelException("Condition is invalid.");
        }
        this.condition = condition;
    }

    public Condition and(Condition other) {
        try {
            // This and other are always valid
            return new Condition("(" + condition + ") & (" + other.condition + ")");
        } catch (ModelException e) {
            return null;
        }
    }

    public Condition not() {
        try {
            // This and other are always valid
            return new Condition("! (" + condition + ")");
        } catch (ModelException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return condition;
    }
}
