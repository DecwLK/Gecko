package org.gecko.model;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

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
    public State(@JsonProperty("id") int id, @JsonProperty("name") String name) {
        super(id);
        this.name = name;
        this.contracts = new HashSet<>();
    }

    public void addContract(Contract contract) {
        contracts.add(contract);
    }

    public void addContracts(Set<Contract> contracts) {
        this.contracts.addAll(contracts);
    }

    public void removeContract(Contract contract) {
        contracts.remove(contract);
    }

    public void removeContracts(Set<Contract> contracts) {
        this.contracts.removeAll(contracts);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
