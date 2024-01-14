package org.gecko.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class GeckoModel {
    @Setter
    private System root;
    private final ModelFactory modelFactory;

    public GeckoModel() {
        this.modelFactory = new ModelFactory();
        this.root = new System("root", "code", new Automaton()); //TODO temporary. how is the root initialized?
    }
}
