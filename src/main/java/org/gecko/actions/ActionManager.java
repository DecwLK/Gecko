package org.gecko.actions;

import java.util.ArrayDeque;
import javafx.scene.control.Alert;
import lombok.Getter;
import org.gecko.exceptions.GeckoException;
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
