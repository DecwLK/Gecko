package org.gecko.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class System implements Renamable, Element {
    private final List<System> children;
    private final List<SystemConnection> connections;
    private final List<Variable> variables;
    private String name;
    private System parent;
    private String code;
    private Automaton automaton;

    public System(String name, String code, Automaton automaton) {
        this.name = name;
        this.code = code;
        this.automaton = automaton;
        this.children = new ArrayList<>();
        this.connections = new ArrayList<>();
        this.variables = new ArrayList<>();
    }

    public void addChild(System child) {
        children.add(child);
    }

    public void addChildren(List<System> children) {
        this.children.addAll(children);
    }

    public void removeChild(System child) {
        children.remove(child);
    }

    public void removeChildren(List<System> children) {
        this.children.removeAll(children);
    }

    public void addConnection(SystemConnection connection) {
        connections.add(connection);
    }

    public void addConnections(List<SystemConnection> connections) {
        this.connections.addAll(connections);
    }

    public void removeConnection(SystemConnection connection) {
        connections.remove(connection);
    }

    public void removeConnections(List<SystemConnection> connections) {
        this.connections.removeAll(connections);
    }

    public void addVariable(Variable variable) {
        variables.add(variable);
    }

    public void addVariables(List<Variable> variables) {
        this.variables.addAll(variables);
    }

    public void removeVariable(Variable variable) {
        variables.remove(variable);
    }

    public void removeVariables(List<Variable> variables) {
        this.variables.removeAll(variables);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }

    public List<Element> getAllElements() {
        List<Element> allElements = new ArrayList<>();
        allElements.addAll(children);
        allElements.addAll(variables);
        allElements.addAll(connections);
        return allElements;
    }
}
