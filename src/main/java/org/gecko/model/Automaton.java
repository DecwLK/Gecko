package org.gecko.model;

import lombok.Getter;

import java.util.List;

@Getter
public class Automaton {
    private State startState;
    private List<Region> regions;
    private List<State> states;
    private List<Edge> edges;

    public Automaton(State startState) {
        //TODO stub
        this.startState = startState;
    }
}
