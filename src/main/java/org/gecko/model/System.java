package org.gecko.model;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.gecko.exceptions.ModelException;

/**
 * Represents a system in the domain model of a Gecko project. A {@link System} has a name, a parent-{@link System}, a
 * set of children-{@link System}s and an {@link Automaton}. It is also described by a code snippet (?), a set of
 * {@link Variable}s, a set of {@link SystemConnection}s connecting the variables. Contains methods for managing the
 * afferent data.
 */
@Getter
@Setter
public class System extends Element implements Renamable {
    private final Set<System> children;
    private final Set<SystemConnection> connections;
    private final Set<Variable> variables;
    private String name;
    @JsonIgnore
    private System parent;
    private String code;
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

    public void setName(String name) throws ModelException {
        if (name == null || name.isEmpty()) {
            throw new ModelException("System's name is invalid.");
        }
        this.name = name;
    }

    public void setAutomaton(Automaton automaton) throws ModelException {
        if (automaton == null) {
            throw new ModelException("Automaton is null.");
        }
        this.automaton = automaton;
    }

    public void addChild(System child) throws ModelException {
        if (child == null || children.contains(child)) {
            throw new ModelException("Cannot add child to system.");
        }
        children.add(child);
    }

    public void addChildren(Set<System> children) throws ModelException {
        for (System child : children) {
            addChild(child);
        }
    }

    public void removeChild(System child) throws ModelException {
        if (child == null || !children.contains(child)) {
            throw new ModelException("Cannot remove child from system.");
        }
        children.remove(child);
    }

    public void removeChildren(Set<System> children) throws ModelException {
        for (System child : children) {
            removeChild(child);
        }
    }

    public void addConnection(SystemConnection connection) throws ModelException {
        if (connection == null || connections.contains(connection)) {
            throw new ModelException("Cannot add connection to system.");
        }
        connections.add(connection);
    }

    public void addConnections(Set<SystemConnection> connections) throws ModelException {
        for (SystemConnection connection : connections) {
            addConnection(connection);
        }
    }

    public void removeConnection(SystemConnection connection) throws ModelException {
        if (connection == null || !connections.contains(connection)) {
            throw new ModelException("Cannot remove connection from system.");
        }
        connection.getDestination().setHasIncomingConnection(false);
        connections.remove(connection);
    }

    public void removeConnections(Set<SystemConnection> connections) throws ModelException {
        for (SystemConnection connection : connections) {
            removeConnection(connection);
        }
    }

    public void addVariable(Variable variable) throws ModelException {
        if (variable == null || variables.contains(variable)) {
            throw new ModelException("Cannot add variable to system.");
        }
        variables.add(variable);
    }

    public void addVariables(Set<Variable> variables) throws ModelException {
        for (Variable variable : variables) {
            addVariable(variable);
        }
    }

    public void removeVariable(Variable variable) throws ModelException {
        if (variable == null || !variables.contains(variable)) {
            throw new ModelException("Cannot remove variable from system.");
        }
        variables.remove(variable);
    }

    public void removeVariables(Set<Variable> variables) throws ModelException {
        for (Variable variable : variables) {
            removeVariable(variable);
        }
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
