package org.gecko.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.exceptions.ModelException;

/**
 * Represents a system in the domain model of a Gecko project. A {@link System} has a name, a parent-{@link System}, a
 * set of children-{@link System}s and an {@link Automaton}. It is also described by a code snippet, a set of
 * {@link Variable}s and a set of {@link SystemConnection}s connecting the variables. Contains methods for managing the
 * afferent data.
 */
@Getter
public class System extends Element implements Renamable {
    private final Set<System> children;
    private final Set<SystemConnection> connections;
    private final Set<Variable> variables;
    private String name;
    @JsonIgnore
    @Setter(onMethod_ = {@NonNull})
    private System parent;
    private String code;
    @Setter(onMethod_ = {@NonNull})
    private Automaton automaton;

    @JsonCreator
    public System(
        @JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("code") String code,
        @JsonProperty("automaton") Automaton automaton) throws ModelException {
        super(id);
        setName(name);
        setCode(code);
        setAutomaton(automaton);
        this.children = new HashSet<>();
        this.connections = new HashSet<>();
        this.variables = new HashSet<>();
    }

    @Override
    public void setName(@NonNull String name) throws ModelException {
        if (name.isEmpty()) {
            throw new ModelException("System's name is invalid.");
        }
        this.name = name;
    }

    public void setCode(String code) throws ModelException {
        if (code != null && code.isEmpty()) {
            throw new ModelException("System's code is invalid.");
        }
        this.code = code;
    }

    public void addChild(@NonNull System child) {
        children.add(child);
    }

    public void addChildren(@NonNull Set<System> children) {
        for (System child : children) {
            addChild(child);
        }
    }

    public void removeChild(@NonNull System child) {
        children.remove(child);
    }

    public void removeChildren(@NonNull Set<System> children) {
        for (System child : children) {
            removeChild(child);
        }
    }

    public void addConnection(@NonNull SystemConnection connection) {
        connections.add(connection);
    }

    public void addConnections(@NonNull Set<SystemConnection> connections) {
        for (SystemConnection connection : connections) {
            addConnection(connection);
        }
    }

    public void removeConnection(@NonNull SystemConnection connection) {
        connection.getDestination().setHasIncomingConnection(false);
        connections.remove(connection);
    }

    public void removeConnections(@NonNull Set<SystemConnection> connections) {
        for (SystemConnection connection : connections) {
            removeConnection(connection);
        }
    }

    public void addVariable(@NonNull Variable variable) {
        variables.add(variable);
    }

    public void addVariables(@NonNull Set<Variable> variables) {
        for (Variable variable : variables) {
            addVariable(variable);
        }
    }

    public void removeVariable(@NonNull Variable variable) {
        variables.remove(variable);
    }

    public void removeVariables(@NonNull Set<Variable> variables) {
        for (Variable variable : variables) {
            removeVariable(variable);
        }
    }

    public System getChildByName(String name) {
        return children.stream().filter(child -> child.getName().equals(name)).findFirst().orElse(null);
    }

    public Variable getVariableByName(String name) {
        return variables.stream().filter(variable -> variable.getName().equals(name)).findFirst().orElse(null);
    }

    /**
     * Returns all children of this system, including the children of the children, and so on.
     *
     * @return a list of all children of this system
     */
    @JsonIgnore
    public List<System> getAllChildren() {
        return children.stream()
            .flatMap(child -> Stream.concat(Stream.of(child), child.getAllChildren().stream()))
            .toList();
    }

    @Override
    public void accept(ElementVisitor visitor) throws ModelException {
        visitor.visit(this);
    }

    @JsonIgnore
    public Set<Element> getAllElements() {
        Set<Element> allElements = new HashSet<>();
        allElements.addAll(children);
        allElements.addAll(variables);
        allElements.addAll(connections);
        return allElements;
    }

    @JsonIgnore
    public System getChildSystemWithVariable(Variable variable) {
        return children.stream().filter(child -> child.getVariables().contains(variable)).findFirst().orElse(null);
    }
}
