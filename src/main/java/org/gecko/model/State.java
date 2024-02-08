package org.gecko.model;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.gecko.exceptions.ModelException;

/**
 * Represents a state in the domain model of a Gecko project. A {@link State} has a name and a set of {@link Contract}s.
 * Contains methods for managing the afferent data.
 */
@Getter
@Setter
public class State extends Element implements Renamable {
    private String name;
    private final Set<Contract> contracts;

    @JsonCreator
    public State(@JsonProperty("id") int id, @JsonProperty("name") String name) throws ModelException {
        super(id);
        setName(name);
        this.contracts = new HashSet<>();
    }

    public void setName(String name) throws ModelException {
        if (name == null || name.isEmpty()) {
            throw new ModelException("State's name is invalid.");
        }
        this.name = name;
    }

    public void addContract(Contract contract) throws ModelException {
        if (contract == null || contracts.contains(contract)) {
            throw new ModelException("Cannot add contract to state.");
        }
        contracts.add(contract);
    }

    public void addContracts(Set<Contract> contracts) throws ModelException {
        for (Contract contract : contracts) {
            addContract(contract);
        }
    }

    public void removeContract(Contract contract) throws ModelException {
        if (contract == null || !contracts.contains(contract)) {
            throw new ModelException("Cannot remove contract from state.");
        }
        contracts.remove(contract);
    }

    public void removeContracts(Set<Contract> contracts) throws ModelException {
        for (Contract contract : contracts) {
            removeContract(contract);
        }
    }

    @Override
    public void accept(ElementVisitor visitor) throws ModelException {
        visitor.visit(this);
    }
}
