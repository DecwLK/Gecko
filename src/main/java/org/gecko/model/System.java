package org.gecko.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Data;

@Data
public class System implements Renamable, Element {
    private final Set<System> children;
    private final Set<SystemConnection> connections;
    private final Set<Variable> variables;
    private String name;
    private System parent;
    private String code;
    private Automaton automaton;

    public System(String name, String code, Automaton automaton) {
        this.name = name;
        this.code = code;
        this.automaton = automaton;
        this.children = new HashSet<>();
        this.connections = new HashSet<>();
        this.variables = new HashSet<>();
    }

    public void addChild(System child) {
        children.add(child);
    }

    public void addChildren(Set<System> children) {
        this.children.addAll(children);
    }

    public void removeChild(System child) {
        children.remove(child);
    }

    public void removeChildren(Set<System> children) {
        this.children.removeAll(children);
    }

    public void addConnection(SystemConnection connection) {
        connections.add(connection);
    }

    public void addConnections(Set<SystemConnection> connections) {
        this.connections.addAll(connections);
    }

    public void removeConnection(SystemConnection connection) {
        connections.remove(connection);
    }

    public void removeConnections(Set<SystemConnection> connections) {
        this.connections.removeAll(connections);
    }

    public void addVariable(Variable variable) {
        variables.add(variable);
    }

    public void addVariables(Set<Variable> variables) {
        this.variables.addAll(variables);
    }

    public void removeVariable(Variable variable) {
        variables.remove(variable);
    }

    public void removeVariables(Set<Variable> variables) {
        this.variables.removeAll(variables);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }

    public Set<Element> getAllElements() {
        Set<Element> allElements = new HashSet<>();
        allElements.addAll(children);
        allElements.addAll(variables);
        allElements.addAll(connections);
        return allElements;
    }
}
