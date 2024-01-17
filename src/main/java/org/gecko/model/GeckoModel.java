package org.gecko.model;

import lombok.Getter;

@Getter
public class GeckoModel {
    private final System root;
    private final ModelFactory modelFactory;

    public GeckoModel() {
        this.modelFactory = new ModelFactory();
        this.root = modelFactory.createRoot();
        this.root.setName("root");
    }

    public GeckoModel(System root) {
        this.modelFactory = new ModelFactory();
        this.root = root;
    }
}
