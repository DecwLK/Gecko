package org.gecko.model;

import lombok.Getter;

/**
 * Represents the Model component of a Gecko project. Holds the root-{@link System} and a {@link ModelFactory}, which
 * allow the creation of and access to Gecko's elements, the data and dependencies of which are required for their
 * eventual graphic representation.
 */
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
