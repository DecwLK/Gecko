package org.gecko.model;

public class ModelFactory {

    //TODO defaults are temporary and need to be changed
    private static int elementId = 0;
    private static final String DEFAULT_NAME = "Element_%d";
    private static final String DEFAULT_TYPE = "int";
    private static final String DEFAULT_CONDITION = "true";
    private static final Kind DEFAULT_KIND = Kind.HIT;
    private static final int DEFAULT_PRIORITY = 0;
    private static final String DEFAULT_CODE = null;
    private static final Visibility DEFAULT_VISIBILITY = Visibility.INPUT;

    private static String getDefaultName(int id) {
        return DEFAULT_NAME.formatted(id);
    }

    private static Contract getDefaultContract() {
        int id = getNewElementId();
        return new Contract(id, getDefaultName(id), new Condition(DEFAULT_CONDITION), new Condition(DEFAULT_CONDITION));
    }

    private static int getNewElementId() {
        return elementId++;
    }

    public State createState(Automaton automaton) {
        int id = getNewElementId();
        State state = new State(id, DEFAULT_NAME.formatted(id));
        automaton.addState(state);
        return state;
    }

    public Edge createEdge(Automaton automaton, State source, State destination) {
        if (!automaton.getStates().contains(source) || !automaton.getStates().contains(destination)) {
            throw new IllegalArgumentException("Source and destination states must be in the automaton"); //TODO better exception
        }
        int id = getNewElementId();
        Edge edge = new Edge(id, source, destination, getDefaultContract(), DEFAULT_KIND, DEFAULT_PRIORITY);
        automaton.addEdge(edge);
        return edge;
    }

    public System createSystem(System parentSystem) {
        int id = getNewElementId();
        System system = new System(id, getDefaultName(id), DEFAULT_CODE, new Automaton());
        parentSystem.addChild(system);
        return system;
    }

    public System createRoot() {
        int id = getNewElementId();
        return new System(id, getDefaultName(id), DEFAULT_CODE, new Automaton());
    }

    public Variable createVariable(System system) {
        int id = getNewElementId();
        Variable variable = new Variable(id, getDefaultName(id), DEFAULT_TYPE, DEFAULT_VISIBILITY);
        system.addVariable(variable);
        return variable;
    }

    public SystemConnection createSystemConnection(System system, Variable source, Variable destination) {
        if (!system.getVariables().contains(source) || !system.getVariables().contains(destination)) {
            throw new IllegalArgumentException("Source and destination variables must be in the system"); //TODO better exception
        }
        int id = getNewElementId();
        SystemConnection connection = new SystemConnection(id, source, destination);
        system.addConnection(connection);
        return connection;
    }

    public Contract createContract(State state) {
        int id = getNewElementId();
        Contract contract = new Contract(id, getDefaultName(id), new Condition(DEFAULT_CONDITION), new Condition(DEFAULT_CONDITION));
        state.addContract(contract);
        return contract;
    }

    public Region createRegion(Automaton automaton) {
        int id = getNewElementId();
        Region region = new Region(id, getDefaultName(id), new Condition(DEFAULT_CONDITION), getDefaultContract());
        automaton.addRegion(region);
        return region;
    }

    public Automaton createAutomaton(System system) {
        Automaton automaton = new Automaton();
        system.setAutomaton(automaton);
        return automaton;
    }
}
