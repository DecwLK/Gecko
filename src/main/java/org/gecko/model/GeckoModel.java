package org.gecko.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class GeckoModel {
    private final System root;
    private final ModelFactory modelFactory;

    public GeckoModel() {
        this.modelFactory = new ModelFactory();
        this.root = new System("root", "code", new Automaton());
    }

    public GeckoModel(System root) {
        this.modelFactory = new ModelFactory();
        this.root = root;
    }
}
