package org.gecko.model;

import lombok.NonNull;
import lombok.Setter;
import org.gecko.exceptions.ModelException;

/**
 * Represents a factory for the model elements of a Gecko project. Provides a method for the creation of each element.
 */
public class ModelFactory {

    //TODO defaults are temporary and need to be changed
    private final GeckoModel geckoModel;
    @Setter
    private int elementId = 0;
    private static final String DEFAULT_NAME = "Element_%d";
    private static final String DEFAULT_TYPE = "int";
    private static final String DEFAULT_CONDITION = "true";
    private static final Kind DEFAULT_KIND = Kind.HIT;
    private static final int DEFAULT_PRIORITY = 0;
    private static final String DEFAULT_CODE = null;
    private static final Visibility DEFAULT_VISIBILITY = Visibility.INPUT;

    public ModelFactory(GeckoModel geckoModel) {
        this.geckoModel = geckoModel;
    }

    private String getDefaultName(int id) {
        String name = DEFAULT_NAME.formatted(id);
        while (!geckoModel.isNameUnique(name)) {
            id++;
            name = DEFAULT_NAME.formatted(id);
        }
        return name;
    }

    private Contract getDefaultContract() throws ModelException {
        int id = getNewElementId();
        return new Contract(id, getDefaultName(id), new Condition(DEFAULT_CONDITION), new Condition(DEFAULT_CONDITION));
    }

    private int getNewElementId() {
        return elementId++;
    }

    public State createState(@NonNull Automaton automaton) throws ModelException {
        int id = getNewElementId();
        State state = new State(id, getDefaultName(id));
        automaton.addState(state);
        return state;
    }

    public State copyState(@NonNull State state) throws ModelException {
        int id = getNewElementId();
        State copy = new State(id, getDefaultName(id));
        for (Contract contract : state.getContracts()) {
            copy.addContract(copyContract(contract));
        }
        return copy;
    }

    public Edge createEdge(@NonNull Automaton automaton, @NonNull State source, @NonNull State destination)
        throws ModelException {
        int id = getNewElementId();
        Edge edge = new Edge(id, source, destination, getDefaultContract(), DEFAULT_KIND, DEFAULT_PRIORITY);
        automaton.addEdge(edge);
        return edge;
    }

    public Edge copyEdge(@NonNull Edge edge) throws ModelException {
        //TODO not functional
        int id = getNewElementId();
        return new Edge(id, edge.getSource(), edge.getDestination(), copyContract(edge.getContract()), edge.getKind(), edge.getPriority());
    }

    public System createSystem(@NonNull System parentSystem) throws ModelException {
        int id = getNewElementId();
        System system = new System(id, getDefaultName(id), DEFAULT_CODE, new Automaton());
        parentSystem.addChild(system);
        system.setParent(parentSystem);
        return system;
    }

    public System copySystem(@NonNull System system) throws ModelException {
        int id = getNewElementId();
        System copy = new System(id, getDefaultName(id), system.getCode(), copyAutomaton(system.getAutomaton()));
        for (System child : system.getChildren()) {
            try {
                copy.addChild(copySystem(child));
            } catch (ModelException e) {
                e.printStackTrace();
            }
        }
        return copy;
    }

    public System createRoot() throws ModelException {
        int id = getNewElementId();
        return new System(id, getDefaultName(id), DEFAULT_CODE, new Automaton());
    }

    public Variable createVariable(@NonNull System system) throws ModelException {
        int id = getNewElementId();
        Variable variable = new Variable(id, getDefaultName(id), DEFAULT_TYPE, DEFAULT_VISIBILITY);
        system.addVariable(variable);
        return variable;
    }

    public SystemConnection createSystemConnection(
        @NonNull System system, @NonNull Variable source, @NonNull Variable destination) throws ModelException {
        int id = getNewElementId();
        SystemConnection connection = new SystemConnection(id, source, destination);
        system.addConnection(connection);
        return connection;
    }

    public Contract createContract(@NonNull State state) throws ModelException {
        int id = getNewElementId();
        Contract contract =
            new Contract(id, getDefaultName(id), new Condition(DEFAULT_CONDITION), new Condition(DEFAULT_CONDITION));
        state.addContract(contract);
        return contract;
    }

    public Contract copyContract(@NonNull Contract contract) throws ModelException {
        int id = getNewElementId();
        return new Contract(id, getDefaultName(id), new Condition(contract.getPreCondition().getCondition()), new Condition(contract.getPostCondition().getCondition()));
    }

    public Region createRegion(@NonNull Automaton automaton) throws ModelException {
        int id = getNewElementId();
        Region region = new Region(id, getDefaultName(id), new Condition(DEFAULT_CONDITION), getDefaultContract());
        automaton.addRegion(region);
        return region;
    }

    public Region copyRegion(@NonNull Region region) throws ModelException {
        int id = getNewElementId();
        return new Region(id, getDefaultName(id), new Condition(region.getInvariant().getCondition()), copyContract(region.getPreAndPostCondition()));
    }

    public Automaton createAutomaton(@NonNull System system) throws ModelException {
        Automaton automaton = new Automaton();
        system.setAutomaton(automaton);
        return automaton;
    }

    public Automaton copyAutomaton (@NonNull Automaton automaton) throws ModelException {
        Automaton copy = new Automaton();
        for (State state : automaton.getStates()) {
            copy.addState(copyState(state));
        }
        for (Edge edge : automaton.getEdges()) {
            copy.addEdge(copyEdge(edge));
        }
        for (Region region : automaton.getRegions()) {
            copy.addRegion(copyRegion(region));
        }
        return copy;
    }

    public Condition createCondition(@NonNull String init) throws ModelException {
        return new Condition(init);
    }
}
