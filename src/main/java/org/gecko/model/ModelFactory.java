package org.gecko.model;

import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.exceptions.ModelException;

/**
 * Represents a factory for the model elements of a Gecko project. Provides a method for the creation of each element.
 */
public class ModelFactory {

    private final GeckoModel geckoModel;
    @Setter
    private int elementId = 0;
    private static final String DEFAULT_NAME = "Element_%d";
    private static final String DEFAULT_TYPE = "int";
    private static final String DEFAULT_CONDITION = Condition.trueCondition().getCondition();
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

    public Pair<State, Map<Contract, Contract>> copyState(@NonNull State state) {
        int id = getNewElementId();
        Map<Contract, Contract> contractToCopy = new HashMap<>();
        State copy;
        try {
            copy = new State(id, getDefaultName(id));
        } catch (ModelException e) {
            throw new RuntimeException("Failed to create a copy of the state", e);
        }
        for (Contract contract : state.getContracts()) {
            Contract copiedContract = copyContract(contract);
            copy.addContract(copiedContract);
            contractToCopy.put(contract, copiedContract);
        }
        return new Pair<>(copy, contractToCopy);
    }

    public Edge createEdge(@NonNull Automaton automaton, @NonNull State source, @NonNull State destination)
        throws ModelException {
        if (automaton.getStates().isEmpty() || !automaton.getStates().contains(source) || !automaton.getStates()
            .contains(destination)) {
            throw new RuntimeException("Failed to create edge, because source and / or destination states "
                + "are not part of the automaton.");
        }

        int id = getNewElementId();
        Edge edge = new Edge(id, source, destination, getDefaultContract(), DEFAULT_KIND, DEFAULT_PRIORITY);
        automaton.addEdge(edge);
        return edge;
    }

    public Edge copyEdge(@NonNull Edge edge) {
        int id = getNewElementId();
        try {
            return new Edge(id, edge.getSource(), edge.getDestination(), edge.getContract(), edge.getKind(),
                edge.getPriority());
        } catch (ModelException e) {
            throw new RuntimeException("Failed to create a copy of an edge", e);
        }
    }

    public System createSystem(@NonNull System parentSystem) throws ModelException {
        int id = getNewElementId();
        System system = new System(id, getDefaultName(id), DEFAULT_CODE, new Automaton());
        parentSystem.addChild(system);
        system.setParent(parentSystem);
        return system;
    }

    public Pair<System, Map<Element, Element>> copySystem(@NonNull System system) throws ModelException {
        return copySystem(system, new HashMap<>());
    }

    public Pair<System, Map<Element, Element>> copySystem(@NonNull System system, Map<Element, Element> originalToCopy)
        throws ModelException {
        int id = getNewElementId();
        System copy;
        try {
            copy = new System(id, getDefaultName(id), DEFAULT_CODE, new Automaton());
        } catch (ModelException e) {
            throw new RuntimeException("Failed to create a copy of the system", e);
        }
        for (State state : system.getAutomaton().getStates()) {
            Pair<State, Map<Contract, Contract>> copyResult = copyState(state);
            State copiedState = copyResult.getKey();
            originalToCopy.putAll(copyResult.getValue());
            copy.getAutomaton().addState(copiedState);
            if (system.getAutomaton().getStartState().equals(state)) {
                copy.getAutomaton().setStartState(copiedState);
            }
            originalToCopy.put(state, copiedState);
        }
        for (Edge edge : system.getAutomaton().getEdges()) {
            State copiedSource = (State) originalToCopy.get(edge.getSource());
            State copiedDestination = (State) originalToCopy.get(edge.getDestination());
            Edge copiedEdge = createEdge(copy.getAutomaton(), copiedSource, copiedDestination);
            copiedEdge.setContract((Contract) originalToCopy.get(edge.getContract()));
            copiedEdge.setKind(edge.getKind());
        }
        for (Region region : system.getAutomaton().getRegions()) {
            Region copiedRegion = copyRegion(region);
            copy.getAutomaton().addRegion(copiedRegion);
            originalToCopy.put(region, copiedRegion);
        }
        for (Variable variable : system.getVariables()) {
            Variable copiedVariable = copyVariable(variable);
            originalToCopy.put(variable, copiedVariable);
            copy.addVariable(copiedVariable);
        }
        for (System childSystem : system.getChildren()) {
            System copiedChildSystem = copySystem(childSystem, originalToCopy).getKey();
            copy.addChild(copiedChildSystem);
            originalToCopy.put(childSystem, copiedChildSystem);

        }
        for (System childSystem : copy.getChildren()) {
            childSystem.setParent(copy);
        }
        for (SystemConnection connection : system.getConnections()) {
            SystemConnection copiedConnection =
                copySystemConnection(connection, (Variable) originalToCopy.get(connection.getSource()),
                    (Variable) originalToCopy.get(connection.getDestination()));
            copy.addConnection(copiedConnection);
            originalToCopy.put(connection, copiedConnection);
        }
        copy.setCode(system.getCode());
        originalToCopy.put(system, copy);
        return new Pair<>(copy, originalToCopy);
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

    public Variable copyVariable(Variable variable) {
        int id = getNewElementId();
        try {
            return new Variable(id, getDefaultName(id), variable.getType(), variable.getVisibility());
        } catch (ModelException e) {
            throw new RuntimeException("Failed to create a copy of a variable", e);
        }
    }

    public SystemConnection createSystemConnection(
        @NonNull System system, @NonNull Variable source, @NonNull Variable destination) throws ModelException {
        System sourceParent = geckoModel.getSystemWithVariable(source);
        System destinationParent = geckoModel.getSystemWithVariable(destination);

        if (sourceParent == null || destinationParent == null) {
            throw new RuntimeException("Failed to create system connection, because source and / or destination "
                + "variables are not part of the model.");
        }

        if (source.equals(destination)) {
            throw new ModelException("A system connection cannot connect a variable to itself.");
        }

        if (sourceParent.equals(system) && sourceParent.equals(destinationParent)) {
            throw new ModelException("Two variables on the same level cannot be connected.");
        }

        if (!isConnectingAllowed(system, sourceParent, destinationParent, source, destination)) {
            throw new ModelException("Failed to connect the source to the destination variable.");
        }

        int id = getNewElementId();
        SystemConnection connection = new SystemConnection(id, source, destination);
        system.addConnection(connection);
        return connection;
    }

    private boolean isConnectingAllowed(
        System system, System sourceParent, System destinationParent, Variable source, Variable destination) {
        if (!sourceParent.equals(system) && !destinationParent.equals(system)) {
            if (!sourceParent.getParent().equals(system) || !destinationParent.getParent().equals(system)) {
                return false;
            }
            return source.getVisibility() == Visibility.OUTPUT && destination.getVisibility() == Visibility.INPUT;
        } else if (sourceParent.equals(system)) {
            return source.getVisibility() != Visibility.OUTPUT && destination.getVisibility() != Visibility.OUTPUT;
        }
        return source.getVisibility() != Visibility.INPUT && destination.getVisibility() != Visibility.INPUT;
    }

    public SystemConnection copySystemConnection(SystemConnection connection) {
        int id = getNewElementId();
        try {
            return new SystemConnection(id, connection.getSource(), connection.getDestination());
        } catch (ModelException e) {
            throw new RuntimeException("Failed to create a copy of a system connection", e);
        }
    }

    public SystemConnection copySystemConnection(
        SystemConnection connection, Variable copiedSource, Variable copiedDestination) {
        SystemConnection result = copySystemConnection(connection);
        try {
            result.setSource(copiedSource);
            result.setDestination(copiedDestination);
        } catch (ModelException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Contract createContract(@NonNull State state) throws ModelException {
        int id = getNewElementId();
        Contract contract =
            new Contract(id, getDefaultName(id), new Condition(DEFAULT_CONDITION), new Condition(DEFAULT_CONDITION));
        state.addContract(contract);
        return contract;
    }

    public Contract copyContract(@NonNull Contract contract) {
        int id = getNewElementId();
        try {
            return new Contract(id, getDefaultName(id), new Condition(contract.getPreCondition().getCondition()),
                new Condition(contract.getPostCondition().getCondition()));
        } catch (ModelException e) {
            throw new RuntimeException("Failed to create a copy of a contract", e);
        }
    }

    public Region createRegion(@NonNull Automaton automaton) throws ModelException {
        int id = getNewElementId();
        Region region = new Region(id, getDefaultName(id), new Condition(DEFAULT_CONDITION), getDefaultContract());
        automaton.addRegion(region);
        return region;
    }

    public Region copyRegion(@NonNull Region region) {
        int id = getNewElementId();
        try {
            return new Region(id, getDefaultName(id), new Condition(region.getInvariant().getCondition()),
                copyContract(region.getPreAndPostCondition()));
        } catch (ModelException e) {
            throw new RuntimeException("Failed to create a copy of a region", e);
        }
    }

    public Condition createCondition(@NonNull String init) throws ModelException {
        return new Condition(init);
    }
}
