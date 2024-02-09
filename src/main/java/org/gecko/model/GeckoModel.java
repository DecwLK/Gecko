package org.gecko.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.gecko.exceptions.ModelException;

/**
 * Represents the Model component of a {@link org.gecko.application.Gecko Gecko}.
 * Holds the root-{@link System} and a {@link ModelFactory}, which allow the creation of and access to Gecko's elements,
 * the data and dependencies of which are required for their eventual graphic representation.
 */
@Getter
public class GeckoModel {
    private final System root;
    private final ModelFactory modelFactory;
    @Setter
    private String globalCode;
    @Setter
    private String globalDefines;


    public GeckoModel() throws ModelException {
        this.modelFactory = new ModelFactory();
        this.root = modelFactory.createRoot();
        this.root.setName("root");
    }

    public GeckoModel(System root) {
        this.modelFactory = new ModelFactory();
        this.root = root;
    }

    public List<System> getAllSystems() {
        List<System> result = new ArrayList<>();
        result.add(root);
        result.addAll(root.getAllChildren());
        return result;
    }
}
