package org.gecko.actions;

import java.util.ArrayDeque;
import lombok.Getter;
import org.gecko.viewmodel.GeckoViewModel;

public class ActionManager {
    @Getter
    private final ActionFactory actionFactory;
    private final ArrayDeque<Action> undoStack;
    private final ArrayDeque<Action> redoStack;

    public ActionManager(GeckoViewModel geckoViewModel) {
        this.actionFactory = new ActionFactory(geckoViewModel);
        undoStack = new ArrayDeque<>();
        redoStack = new ArrayDeque<>();
    }

    public void undo() {
        if (undoStack.isEmpty()) {
            return;
        }
        Action action = undoStack.removeFirst();
        action.run();
        redoStack.addFirst(action.getUndoAction(actionFactory));
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            return;
        }
        Action action = redoStack.removeFirst();
        action.run();
        Action undoAction = action.getUndoAction(actionFactory);
        if (undoAction != null) {
            undoStack.addFirst(undoAction);
        }
    }

    public void run(Action action) {
        action.run();
        Action undoAction = action.getUndoAction(actionFactory);
        if (undoAction != null) {
            undoStack.addFirst(undoAction);
        }
        redoStack.clear();
    }
}
