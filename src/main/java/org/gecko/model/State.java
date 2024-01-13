package org.gecko.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class State implements Renamable, Element {
    private String name;
    private final List<Contract> contracts;

    public State(String name) {
        this.name = name;
        this.contracts = new ArrayList<>();
    }

    public void addContract(Contract contract) {
        contracts.add(contract);
    }

    public void addContracts(List<Contract> contracts) {
        this.contracts.addAll(contracts);
    }

    public void removeContract(Contract contract) {
        contracts.remove(contract);
    }

    public void removeContracts(List<Contract> contracts) {
        this.contracts.removeAll(contracts);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
