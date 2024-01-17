package org.gecko.model;

import lombok.Data;

/**
 * Represents a condition in the domain model of a Gecko project.
 */
@Data
public class Condition {
    private String condition;

    public Condition(String condition) {
        this.condition = condition;
    }

}
