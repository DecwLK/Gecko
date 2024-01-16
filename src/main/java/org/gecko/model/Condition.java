package org.gecko.model;

import lombok.Data;

@Data
public class Condition {
    private String condition;

    public Condition(String condition) {
        this.condition = condition;
    }

}
