package org.gecko.actions;

import java.util.Stack;
import lombok.Getter;

public class ActionManager {
    @Getter
    private final ActionFactory actionFactory;
    private final Stack<Action> undoStack;
    private final Stack<Action> redoStack;

    public ActionManager(ActionFactory actionFactory) {
        this.actionFactory = actionFactory;
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void undo() {
        if (undoStack.isEmpty()) {
            return;
        }
        Action action = undoStack.pop();
        action.run();
        redoStack.push(action.getUndoAction(actionFactory));
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            return;
        }
        Action action = redoStack.pop();
        action.run();
        Action undoAction = action.getUndoAction(actionFactory);
        if (undoAction != null) {
            undoStack.push(undoAction);
        }
    }

    public void run(Action action) {
        action.run();
        Action undoAction = action.getUndoAction(actionFactory);
        if (undoAction != null) {
            undoStack.push(undoAction);
        }
        redoStack.clear();
    }
}
