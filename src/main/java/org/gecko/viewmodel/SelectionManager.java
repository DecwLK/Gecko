package org.gecko.viewmodel;

import java.util.ArrayDeque;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import lombok.Data;

@Data
public class SelectionManager {
    private ArrayDeque<Set<PositionableViewModelElement<?>>> undoSelectionStack;
    private ArrayDeque<Set<PositionableViewModelElement<?>>> redoSelectionStack;
    private ObservableSet<PositionableViewModelElement<?>> currentSelection;

    public SelectionManager() {
        this.undoSelectionStack = new ArrayDeque<>();
        this.redoSelectionStack = new ArrayDeque<>();
        this.currentSelection = FXCollections.observableSet();
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
        select(Set.of(element));
    }

    public void select(Set<PositionableViewModelElement<?>> elements) {
        currentSelection.addAll(elements);
    }

    public void deselect(PositionableViewModelElement<?> element) {
        deselect(Set.of(element));
    }

    public void deselect(Set<PositionableViewModelElement<?>> elements) {
        undoSelectionStack.push(currentSelection);
        redoSelectionStack.clear();
        currentSelection.removeAll(elements);
    }

    public void deselectAll() {
        deselect(currentSelection);
    }
}
