package org.gecko.viewmodel;

import java.util.List;
import java.util.Stack;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import lombok.Data;

@Data
public class SelectionManager {
    private final Stack<List<PositionableViewModelElement<?>>> undoSelectionStack;
    private final Stack<List<PositionableViewModelElement<?>>> redoSelectionStack;
    private final ObservableList<PositionableViewModelElement<?>> currentSelection;

    public SelectionManager() {
        this.undoSelectionStack = new Stack<>();
        this.redoSelectionStack = new Stack<>();
        this.currentSelection = FXCollections.observableArrayList();
    }

    public void goBack() {
        redoSelectionStack.push(currentSelection);
        currentSelection.clear();
        currentSelection.addAll(undoSelectionStack.pop());
    }

    public void goForward() {
        undoSelectionStack.push(currentSelection);
        currentSelection.clear();
        currentSelection.addAll(redoSelectionStack.pop());
    }

    public void select(PositionableViewModelElement<?> element) {
        select(List.of(element));
    }

    public void select(List<PositionableViewModelElement<?>> elements) {
        undoSelectionStack.push(currentSelection);
        redoSelectionStack.clear();
        currentSelection.clear();
        currentSelection.addAll(elements);
    }

    public void deselect(PositionableViewModelElement<?> element) {
        deselect(List.of(element));
    }

    public void deselect(List<PositionableViewModelElement<?>> elements) {
        undoSelectionStack.push(currentSelection);
        redoSelectionStack.clear();
        currentSelection.removeAll(elements);
    }

    public void deselectAll() {
        deselect(currentSelection);
    }
}
