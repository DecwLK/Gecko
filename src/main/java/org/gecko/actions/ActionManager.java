package org.gecko.actions;

import java.util.ArrayDeque;
import javafx.scene.control.Alert;
import lombok.Getter;
import lombok.Setter;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.GeckoViewModel;

/**
 * Represents a manager for the actions of the active {@link org.gecko.application.Gecko Gecko}. Holds an
 * {@link ActionFactory}, a stack of currently undoable {@link Action}s and a stack of currently redoable
 * {@link Action}s, thus providing methods for running, undoing and redoing actions.
 */
public class ActionManager {
    @Getter
    private final ActionFactory actionFactory;
    private final ArrayDeque<Action> undoStack;
    private final ArrayDeque<Action> redoStack;
    private static final int MAX_STACK_SIZE = 1000;

    @Getter
    @Setter
    private CopyPositionableViewModelElementVisitor copyVisitor = null;

    public ActionManager(GeckoViewModel geckoViewModel) {
        this.actionFactory = new ActionFactory(geckoViewModel);
        undoStack = new ArrayDeque<>();
        redoStack = new ArrayDeque<>();
    }

    /**
     * Undoes the last action and makes it redoable.
     */
    public void undo() {
        if (undoStack.isEmpty()) {
            return;
        }
        Action action = undoStack.removeFirst();
        try {
            if (!action.run()) {
                return;
            }
        } catch (GeckoException e) {
            showExceptionAlert(e.getMessage());
            return;
        }
        redoStack.addFirst(action.getUndoAction(actionFactory));
    }

    /**
     * Redoes the last undone action and makes it undoable again.
     */
    public void redo() {
        if (redoStack.isEmpty()) {
            return;
        }
        Action action = redoStack.removeFirst();
        try {
            if (!action.run()) {
                return;
            }
        } catch (GeckoException e) {
            showExceptionAlert(e.getMessage());
            return;
        }
        Action undoAction = action.getUndoAction(actionFactory);
        if (undoAction != null) {
            undoStack.addFirst(undoAction);
        }
    }

    /**
     * Runs the given action and makes it undoable if applicable.
     *
     * @param action The action to run.
     */
    public void run(Action action) {
        try {
            if (!action.run()) {
                return;
            }
        } catch (GeckoException e) {
            showExceptionAlert(e.getMessage());
            return;
        }
        Action undoAction = action.getUndoAction(actionFactory);
        if (undoAction != null) {
            undoStack.addFirst(undoAction);
            if (undoStack.size() > MAX_STACK_SIZE) {
                undoStack.removeLast();
            }
            redoStack.clear();
        }
    }

    public void showExceptionAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
