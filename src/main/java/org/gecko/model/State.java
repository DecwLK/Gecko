package org.gecko.model;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class State extends Element implements Renamable {
    private String name;
    private final Set<Contract> contracts;

    public State(int id, String name) {
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
