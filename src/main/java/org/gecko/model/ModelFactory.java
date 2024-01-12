package org.gecko.model;

public class ModelFactory {
    public State createState(Automaton automaton) {
        //TODO stub
        return new State();
    }

    public Edge createEdge(Automaton automaton, Contract contract, Kind kind, int priority) {
        //TODO stub
        return new Edge(contract, kind, priority);
    }

    public System createSystem(System parentSystem) {
        //TODO stub
        return new System();
    }

    public Variable createVariable(System system) {
        //TODO stub
        return new Variable();
    }

    public SystemConnection createSystemConnection(System system, Variable source, Variable destination) {
        //TODO stub
        return new SystemConnection(source, destination);
    }

    public Contract createContract(State state, Condition preCondition, Condition postCondition) {
        //TODO stub
        return new Contract(preCondition, postCondition);
    }

    public Region createRegion(Automaton automaton, Condition invariant, Contract preAndPostCondition) {
        //TODO stub
        return new Region(invariant, preAndPostCondition);
    }

    public Automaton createAutomaton(System system, State startState) {
        //TODO stub
        return new Automaton(startState);
    }
}
