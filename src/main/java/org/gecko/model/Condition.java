package org.gecko.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import org.gecko.exceptions.ModelException;

/**
 * Represents a condition in the domain model of a Gecko project.
 */
@Data
public class Condition {
    private String condition;

    private static final String AND_CONDITION_REGEX = "(%s) & (%s)";
    private static final String NOT_CONDITION_REGEX = "! (%s)";

    @JsonCreator
    public Condition(@JsonProperty("condition") String condition) throws ModelException {
        setCondition(condition);
    }

    public void setCondition(@NonNull String condition) throws ModelException {
        if (condition.isEmpty()) {
            throw new ModelException("Condition is invalid.");
        }
        this.condition = condition;
    }

    @JsonIgnore
    public Condition and(Condition other) {
        try {
            // This and other are always valid
            return new Condition(AND_CONDITION_REGEX.formatted(condition, other.condition));
        } catch (ModelException e) {
            return null;
        }
    }

    @JsonIgnore
    public Condition not() {
        try {
            // This is always valid
            return new Condition(NOT_CONDITION_REGEX.formatted(condition));
        } catch (ModelException e) {
            return null;
        }
    }

    @JsonIgnore
    @Override
    public String toString() {
        return condition;
    }

    public static Condition trueCondition() {
        //Name has to be this because true is reserved
        try {
            return new Condition("true");
        } catch (ModelException e) {
            return null;
        }
    }
}
