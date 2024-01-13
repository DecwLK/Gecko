package org.gecko.model;

import lombok.Getter;

@Getter
public class Condition {
    private final String condition;

    public Condition(String condition) {
        this.condition = condition;
    }
}
