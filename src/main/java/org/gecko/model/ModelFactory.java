package org.gecko.model;

public class ModelFactory {

    //TODO defaults are temporary and need to be changed
    private static int NUM_ELEMENTS = 0;
    private static final String DEFAULT_NAME = "Element_%d";
    private static final String DEFAULT_TYPE = "int";
    private static final String DEFAULT_CONDITION = "true";
    private static final Kind DEFAULT_KIND = Kind.HIT;
    private static final int DEFAULT_PRIORITY = 0;
    private static final String DEFAULT_CODE = null;
    private static final Visibility DEFAULT_VISIBILITY = Visibility.INPUT;

    private static String getDefaultName() {
        return DEFAULT_NAME.formatted(NUM_ELEMENTS++);
    }

    private static Contract getDefaultContract() {
        return new Contract(getDefaultName(), new Condition(DEFAULT_CONDITION), new Condition(DEFAULT_CONDITION));
    }

    public State createState(Automaton automaton) {
        State state = new State(DEFAULT_NAME.formatted(NUM_ELEMENTS++));
        automaton.addState(state);
        return state;
    }

    public Edge createEdge(Automaton automaton, State source, State destination) {
        if (!automaton.getStates().contains(source) || !automaton.getStates().contains(destination)) {
            throw new IllegalArgumentException("Source and destination states must be in the automaton"); //TODO better exception
        }
        Edge edge = new Edge(source, destination, getDefaultContract(), DEFAULT_KIND, DEFAULT_PRIORITY);
        automaton.addEdge(edge);
        return edge;
    }

    public System createSystem(System parentSystem) {
        System system = new System(getDefaultName(), DEFAULT_CODE, new Automaton());
        parentSystem.addChild(system);
        return system;
    }

    public Variable createVariable(System system) {
        Variable variable = new Variable(getDefaultName(), DEFAULT_TYPE, DEFAULT_VISIBILITY);
        system.addVariable(variable);
        return variable;
    }

    public SystemConnection createSystemConnection(System system, Variable source, Variable destination) {
        if (!system.getVariables().contains(source) || !system.getVariables().contains(destination)) {
            throw new IllegalArgumentException("Source and destination variables must be in the system"); //TODO better exception
        }
        SystemConnection connection = new SystemConnection(source, destination);
        system.addConnection(connection);
        return connection;
    }

    public Contract createContract(State state) {
        Contract contract = new Contract(getDefaultName(), new Condition(DEFAULT_CONDITION), new Condition(DEFAULT_CONDITION));
        state.addContract(contract);
        return contract;
    }

    public Region createRegion(Automaton automaton) {
        Region region = new Region(getDefaultName(), new Condition(DEFAULT_CONDITION), getDefaultContract());
        automaton.addRegion(region);
        return region;
    }

    public Automaton createAutomaton(System system) {
        Automaton automaton = new Automaton();
        system.setAutomaton(automaton);
        return automaton;
    }
}
