package org.gecko.viewmodel;

import java.util.ArrayDeque;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;

@Data
public class SelectionManager {
    private ArrayDeque<List<PositionableViewModelElement<?>>> undoSelectionStack;
    private ArrayDeque<List<PositionableViewModelElement<?>>> redoSelectionStack;
    private ObservableList<PositionableViewModelElement<?>> currentSelection;

    public SelectionManager() {
        this.undoSelectionStack = new ArrayDeque<>();
        this.redoSelectionStack = new ArrayDeque<>();
        this.currentSelection = FXCollections.observableArrayList();
    }

    public void goBack() {
        redoSelectionStack.push(currentSelection);
        currentSelection.clear();
        currentSelection.addAll(undoSelectionStack.pop());
    }

    public void goForward() {
        select(redoSelectionStack.pop());
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
