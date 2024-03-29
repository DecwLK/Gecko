package org.gecko.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.gecko.exceptions.ModelException;
import org.gecko.model.Automaton;
import org.gecko.model.Condition;
import org.gecko.model.Contract;
import org.gecko.model.Edge;
import org.gecko.model.GeckoModel;
import org.gecko.model.Kind;
import org.gecko.model.Region;
import org.gecko.model.State;
import org.gecko.model.System;
import org.gecko.model.SystemConnection;
import org.gecko.model.Variable;

/**
 * The AutomatonFileSerializer is used to export a project to a sys file. When exporting, it transforms features unique
 * to Gecko, such as regions, kinds and priorities, to be compatible with the sys file format.
 */
public class AutomatonFileSerializer implements FileSerializer {
    private final GeckoModel model;

    private static final String INDENT = "    ";
    private static final String SERIALIZED_CONTRACT_NAME = "contract %s";
    private static final String AUTOMATON_SERIALIZATION_AS_SYSTEM_CONTRACT = SERIALIZED_CONTRACT_NAME + " {";
    private static final String SERIALIZED_CONTRACT = SERIALIZED_CONTRACT_NAME + " := %s ==> %s";
    private static final String SERIALIZED_TRANSITION = "%s -> %s :: %s";
    private static final String SERIALIZED_SYSTEM = "reactor %s {";
    private static final String SERIALIZED_CONNECTION = "%s.%s -> %s.%s";
    private static final String SERIALIZED_STATE = "state %s: %s";
    private static final String VARIABLE_ATTRIBUTES = " %s: %s";
    private static final String SERIALIZED_INPUT = "input";
    private static final String SERIALIZED_OUTPUT = "output";
    private static final String SERIALIZED_STATE_VISIBILITY = "state";

    public AutomatonFileSerializer(GeckoModel model) {
        this.model = model;
    }

    @Override
    public void writeToFile(File file) throws IOException {
        StringJoiner joiner = new StringJoiner(java.lang.System.lineSeparator());
        if (model.getGlobalDefines() != null) {
            joiner.add(model.getGlobalDefines());
            joiner.add("");
        }
        if (model.getGlobalCode() != null) {
            joiner.add(serializeCode(model.getGlobalCode()));
            joiner.add("");
        }
        joiner.add(serializeAutomata(model));
        joiner.add(serializeSystems(model));
        Files.writeString(file.toPath(), joiner.toString());
    }

    private String serializeAutomata(GeckoModel model) {
        List<System> relevantSystems =
            model.getAllSystems().stream().filter(system -> !system.getAutomaton().isEmpty()).toList();
        return serializeCollectionWithMapping(relevantSystems, this::serializeAutomaton);
    }

    private String serializeSystems(GeckoModel model) {
        return serializeCollectionWithMapping(model.getAllSystems(), this::serializeSystem);
    }

    private String serializeAutomaton(System system) {
        Automaton automaton = system.getAutomaton();
        StringJoiner joiner = new StringJoiner(java.lang.System.lineSeparator());
        joiner.add(AUTOMATON_SERIALIZATION_AS_SYSTEM_CONTRACT.formatted(system.getName()));
        if (!system.getVariables().isEmpty()) {
            joiner.add(serializeIo(system));
            joiner.add("");
        }
        joiner.add(serializeCollectionWithMapping(automaton.getStates(), this::serializeStateContracts, automaton));
        joiner.add("");
        List<Edge> relevantEdges = automaton.getEdges().stream().filter(edge -> edge.getContract() != null).toList();
        joiner.add(serializeCollectionWithMapping(relevantEdges, this::serializeTransition));
        joiner.add("}");
        joiner.add("");
        return joiner.toString();
    }

    private String serializeStateContracts(State state, Automaton automaton) {
        //Edges are used so much here because contracts don't have priorities or kinds and only states can be in regions
        List<Region> relevantRegions = automaton.getRegionsWithState(state);
        List<Edge> edges =
            automaton.getOutgoingEdges(state).stream().filter(edge -> edge.getContract() != null).toList();
        if (edges.isEmpty()) {
            return "";
        }
        //Creating new contracts to not alter the model
        Map<Edge, Contract> newContracts = new HashMap<>();
        for (Edge edge : edges) {
            Contract newContract = applyRegionsToContract(relevantRegions, edge.getContract());
            try {
                applyKindToContract(newContract, edge.getKind());
                newContract.setName(getContractName(edge));
            } catch (ModelException e) {
                throw new RuntimeException("Failed to apply kind to contract", e);
            }
            newContracts.put(edge, newContract);
        }

        //Building the conditions for the priorities
        List<List<Edge>> groupedEdges =
            new ArrayList<>(edges.stream().collect(Collectors.groupingBy(Edge::getPriority)).values());
        Collections.reverse(groupedEdges);
        List<Condition> preConditionsByPrio = new ArrayList<>();
        for (List<Edge> edgeGroup : groupedEdges) {
            //OrElseThrow because validity needs to be ensured by model
            Condition newPre = edgeGroup.stream()
                .map(newContracts::get)
                .map(Contract::getPreCondition)
                .reduce(Condition::and)
                .orElseThrow();
            preConditionsByPrio.add(newPre);
        }
        //and the specific condition for a prio with all conditions with lower prio
        List<Condition> allLowerPrioPreConditions = new ArrayList<>();
        for (int i = 0; i < preConditionsByPrio.size() - 1; i++) {
            allLowerPrioPreConditions.add(
                allLowerPrioPreConditions.stream().reduce(preConditionsByPrio.get(i), Condition::and));
        }

        //applying priorites
        int prioIndex = 0;
        for (List<Edge> edgeGroup : groupedEdges) {
            for (Edge edge : edgeGroup) {
                if (prioIndex == 0) {
                    continue; //Highest prio doesn't need to be altered
                }
                Contract contractWithPrio = newContracts.get(edge);
                contractWithPrio.setPreCondition(
                    contractWithPrio.getPreCondition().and(allLowerPrioPreConditions.get(prioIndex - 1).not()));
                newContracts.put(edge, contractWithPrio);
            }
            prioIndex++;
        }
        return serializeCollectionWithMapping(newContracts.values(), this::serializeContract);
    }

    private void applyKindToContract(Contract contract, Kind kind) throws ModelException {
        switch (kind) {
            case MISS -> {
                contract.setPreCondition(contract.getPreCondition().not());
                contract.setPostCondition(Condition.trueCondition());
            }
            case FAIL -> contract.setPostCondition(contract.getPostCondition().not());
            case HIT -> {
            }
            default -> throw new IllegalArgumentException("Unknown kind: " + kind);
        }
    }

    private Contract applyRegionsToContract(List<Region> relevantRegions, Contract contract) {
        Contract newContract;
        try {
            newContract = new Contract(0, contract.getName(), contract.getPreCondition(), contract.getPostCondition());
        } catch (ModelException e) {
            throw new RuntimeException("Failed to build contract out of other valid contracts", e);
        }
        if (relevantRegions.isEmpty()) {
            return newContract;
        }
        List<Condition> newConditions = andConditions(relevantRegions);
        newContract.setPreCondition(newConditions.getFirst().and(newContract.getPreCondition()));
        newContract.setPostCondition(newConditions.get(1).and(newContract.getPostCondition()));
        return newContract;
    }

    private List<Condition> andConditions(List<Region> regions) {
        Region first = regions.getFirst();

        Condition newPre;
        Condition newPost;
        try {
            newPre = new Condition(first.getPreAndPostCondition().getPreCondition().getCondition());
            newPost = new Condition(first.getPreAndPostCondition().getPostCondition().getCondition());
        } catch (ModelException e) {
            throw new RuntimeException("Failed to build conditions out of other valid conditions", e);
        }
        newPre = newPre.and(first.getInvariant());
        newPost = newPost.and(first.getInvariant());

        for (int i = 1; i < regions.size(); i++) {
            Region region = regions.get(i);
            newPre = newPre.and(region.getPreAndPostCondition().getPreCondition());
            newPre = newPre.and(region.getInvariant());
            newPost = newPost.and(region.getPreAndPostCondition().getPostCondition());
            newPost = newPost.and(region.getInvariant());
        }
        return List.of(newPre, newPost);
    }

    private String serializeContract(Contract contract) {
        return INDENT + SERIALIZED_CONTRACT.formatted(contract.getName(), contract.getPreCondition(),
            contract.getPostCondition());
    }

    private String serializeTransition(Edge edge) {
        return INDENT + SERIALIZED_TRANSITION.formatted(edge.getSource().getName(), edge.getDestination().getName(),
            getContractName(edge));
    }

    private String serializeSystem(System system) {
        StringJoiner joiner = new StringJoiner(java.lang.System.lineSeparator());
        joiner.add(SERIALIZED_SYSTEM.formatted(system.getName()));
        if (!system.getVariables().isEmpty()) {
            joiner.add(serializeIo(system));
            joiner.add("");
        }
        if (!system.getChildren().isEmpty()) {
            joiner.add(serializeChildren(system));
            joiner.add("");
        }
        if (system.getAutomaton() != null && !system.getAutomaton().isEmpty()) {
            joiner.add(INDENT + SERIALIZED_CONTRACT_NAME.formatted(system.getName()));
            joiner.add("");
        }
        if (!system.getConnections().isEmpty()) {
            joiner.add(serializeConnections(system));
            joiner.add("");
        }
        if (system.getCode() != null) {
            joiner.add(serializeCode(system.getCode()));
        }
        joiner.add("}");
        joiner.add("");
        return joiner.toString();
    }

    private String serializeConnections(System system) {
        return serializeCollectionWithMapping(system.getConnections(), this::serializeConnection, system);
    }

    private String serializeConnection(SystemConnection connection, System parent) {
        String startSystem = serializeSystemReference(parent, connection.getSource());
        String startPort = connection.getSource().getName();
        String endSystem = serializeSystemReference(parent, connection.getDestination());
        String endPort = connection.getDestination().getName();
        return INDENT + SERIALIZED_CONNECTION.formatted(startSystem, startPort, endSystem, endPort);
    }

    private String serializeSystemReference(System parent, Variable var) {
        if (parent.getVariables().contains(var)) {
            return AutomatonFileVisitor.SELF_REFERENCE_TOKEN;
        } else {
            return parent.getChildSystemWithVariable(var).getName();
        }
    }

    private String serializeIo(System system) {
        List<Variable> orderedVariables =
            system.getVariables().stream().sorted(Comparator.comparing(Variable::getVisibility)).toList();
        return serializeCollectionWithMapping(orderedVariables, this::serializeVariable);
    }

    private String serializeChildren(System system) {
        return serializeCollectionWithMapping(system.getChildren(), this::serializeChild);
    }

    private String serializeChild(System system) {
        return INDENT + SERIALIZED_STATE.formatted(system.getName(), system.getName());
    }

    private String serializeVariable(Variable variable) {
        String output = "";
        output += INDENT + switch (variable.getVisibility()) {
            case INPUT -> SERIALIZED_INPUT;
            case OUTPUT -> SERIALIZED_OUTPUT;
            case STATE -> SERIALIZED_STATE_VISIBILITY;
            default -> throw new IllegalArgumentException("Unknown visibility: " + variable.getVisibility());
        };
        output += VARIABLE_ATTRIBUTES.formatted(variable.getName(), variable.getType());
        if (variable.getValue() != null) {
            output += " := " + variable.getValue();
        }
        return output;
    }

    private String serializeCode(String code) {
        return INDENT + AutomatonFileVisitor.CODE_BEGIN + code + AutomatonFileVisitor.CODE_END;
    }

    private <T> String serializeCollectionWithMapping(Collection<T> collection, Function<T, String> mapping) {
        StringJoiner joiner = new StringJoiner(java.lang.System.lineSeparator());
        for (T item : collection) {
            String result = mapping.apply(item);
            if (result == null || result.isBlank()) {
                continue;
            }
            joiner.add(result);
        }
        return joiner.toString();
    }

    private <T, S> String serializeCollectionWithMapping(
        Collection<T> collection, BiFunction<T, S, String> mapping, S additionalArg) {
        StringJoiner joiner = new StringJoiner(java.lang.System.lineSeparator());
        for (T item : collection) {
            String result = mapping.apply(item, additionalArg);
            if (result == null || result.isBlank()) {
                continue;
            }
            joiner.add(result);
        }
        return joiner.toString();
    }

    private String getContractName(Edge edge) {
        String name = edge.getContract().getName();
        if (edge.getKind() == Kind.HIT) {
            return name;
        }
        return makeNameUnique("%s_%s".formatted(name, edge.getKind().name()));
    }

    private String makeNameUnique(String baseName) {
        String name = baseName;
        int i = 1;
        while (!model.isNameUnique(name)) {
            name = "%s_%d".formatted(baseName, i++);
        }
        return name;
    }
}
