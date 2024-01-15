package org.gecko.viewmodel;

import javafx.collections.ObservableList;
import lombok.Data;

import java.util.List;
import java.util.Stack;

@Data
public class SelectionManager {
    private Stack<List<PositionableViewModelElement<?>>> undoSelectionStack;
    private Stack<List<PositionableViewModelElement<?>>> redoSelectionStack;
    private ObservableList<PositionableViewModelElement<?>> currentSelection;

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
