package org.gecko.model;

import lombok.Getter;

@Getter
public class GeckoModel {
    private System root;
    public ModelFactory getModelFactory() {
        //TODO stub
        return new ModelFactory();
    }
}
