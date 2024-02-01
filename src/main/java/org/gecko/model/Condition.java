package org.gecko.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents a condition in the domain model of a Gecko project.
 */
@Data
public class Condition {
    private String condition;

    @JsonCreator
    public Condition(@JsonProperty("condition") String condition) {
        this.condition = condition;
    }
}
