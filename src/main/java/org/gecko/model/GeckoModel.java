package org.gecko.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.exceptions.ModelException;

/**
 * Represents the Model component of a {@link org.gecko.application.Gecko Gecko}. Holds the root-{@link System} and a
 * {@link ModelFactory}, which allow the creation of and access to Gecko's elements, the data and dependencies of which
 * are required for their eventual graphic representation.
 */
@Getter
public class GeckoModel {
    private static final String DEFAULT_NAME = "^Element_\\d+\\b";
    private final System root;
    private final ModelFactory modelFactory;
    @Setter
    private String globalCode;
    @Setter
    private String globalDefines;


    public GeckoModel() throws ModelException {
        this.modelFactory = new ModelFactory();
        this.root = modelFactory.createRoot();
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

    public System getSystemWithVariable(@NonNull Variable variable) {
        return findSystemWithVariable(root, variable);
    }

    private System findSystemWithVariable(@NonNull System system, @NonNull Variable variable) {
        if (system.getVariables().contains(variable)) {
            return system;
        }
        for (System child : system.getChildren()) {
            System result = findSystemWithVariable(child, variable);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public boolean isNameValid(@NonNull String name) {
        return !isNameDefaultName(name) && isNameUnique(name);
    }

    private boolean isNameUnique(@NonNull String name) {
        return isNameUnique(root, name);
    }

    private boolean isNameUnique(@NonNull System system, @NonNull String name) {
        if (system.getName().equals(name)) {
            return false;
        }
        if (system.getVariables().stream().anyMatch(variable -> variable.getName().equals(name))) {
            return false;
        }

        Automaton automaton = system.getAutomaton();
        if (automaton.getRegions().stream().anyMatch(region -> region.getName().equals(name))) {
            return false;
        }
        for (State state : automaton.getStates()) {
            if (state.getName().equals(name)) {
                return false;
            }
            if (state.getContracts().stream().anyMatch(contract -> contract.getName().equals(name))) {
                return false;
            }
        }

        for (System child : system.getChildren()) {
            if (!isNameUnique(child, name)) {
                return false;
            }
        }
        return true;
    }

    private boolean isNameDefaultName(@NonNull String name) {
        return name.matches(DEFAULT_NAME);
    }
}
