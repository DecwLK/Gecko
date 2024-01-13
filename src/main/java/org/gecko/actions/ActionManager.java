package org.gecko.actions;

import lombok.Getter;

import java.util.Stack;

public class ActionManager {
    @Getter
    private ActionFactory actionFactory;
    private Stack<Action> undoStack;
    private Stack<Action> redoStack;

    public ActionManager(ActionFactory actionFactory) {
        this.actionFactory = actionFactory;
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void undo() {
        //TODO stub
    }

    public void redo() {
        //TODO stub
    }

    void run(Action action) {
        //TODO register action if undoable
    }
}
