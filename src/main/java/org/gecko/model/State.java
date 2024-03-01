package org.gecko.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
import org.gecko.exceptions.ModelException;

/**
 * Represents a state in the domain model of a Gecko project. A {@link State} has a name and a set of {@link Contract}s.
 * Contains methods for managing the afferent data.
 */
@Getter
public class State extends Element implements Renamable {

    private String name;
    private final Set<Contract> contracts;

    @JsonCreator
    public State(@JsonProperty("id") int id, @JsonProperty("name") String name) throws ModelException {
        super(id);
        setName(name);
        this.contracts = new HashSet<>();
    }

    @Override
    public void setName(@NonNull String name) throws ModelException {
        if (name.isEmpty()) {
            throw new ModelException("State's name is invalid.");
        }
        this.name = name;
    }

    public void addContract(@NonNull Contract contract) {
        contracts.add(contract);
    }

    public void addContracts(@NonNull Set<Contract> contracts) {
        for (Contract contract : contracts) {
            addContract(contract);
        }
    }

    public void removeContract(@NonNull Contract contract) {
        contracts.remove(contract);
    }

    public void removeContracts(@NonNull Set<Contract> contracts) {
        for (Contract contract : contracts) {
            removeContract(contract);
        }
    }
}
