package org.gecko.viewmodel;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Data;

@Data
public class SelectionManager {
    private ArrayDeque<Set<PositionableViewModelElement<?>>> undoSelectionStack;
    private ArrayDeque<Set<PositionableViewModelElement<?>>> redoSelectionStack;
    private ObjectProperty<Set<PositionableViewModelElement<?>>> currentSelectionProperty;

    public SelectionManager() {
        this.undoSelectionStack = new ArrayDeque<>();
        this.redoSelectionStack = new ArrayDeque<>();
        this.currentSelectionProperty = new SimpleObjectProperty<>(new HashSet<>());
    }

    public void goBack() {
        if (undoSelectionStack.isEmpty()) {
            return;
        }
        redoSelectionStack.push(new HashSet<>(currentSelectionProperty.get()));
        currentSelectionProperty.set(undoSelectionStack.pop());
    }

    public void goForward() {
        if (redoSelectionStack.isEmpty()) {
            return;
        }
        undoSelectionStack.push(new HashSet<>(currentSelectionProperty.get()));
        currentSelectionProperty.set(redoSelectionStack.pop());
    }

    public void select(PositionableViewModelElement<?> element) {
        select(Set.of(element));
    }

    public void select(Set<PositionableViewModelElement<?>> elements) {
        if (elements.isEmpty() || elements.equals(currentSelectionProperty.get())) {
            return;
        }
        redoSelectionStack.clear();
        undoSelectionStack.push(new HashSet<>(currentSelectionProperty.get()));
        currentSelectionProperty.set(elements);
    }

    public void deselect(PositionableViewModelElement<?> element) {
        deselect(Set.of(element));
    }

    public void deselect(Set<PositionableViewModelElement<?>> elements) {
        if (elements.isEmpty() || elements.stream().noneMatch(currentSelectionProperty.get()::contains)) {
            return;
        }
        redoSelectionStack.clear();
        undoSelectionStack.push(new HashSet<>(currentSelectionProperty.get()));
        Set<PositionableViewModelElement<?>> newSelection = new HashSet<>(currentSelectionProperty.get());
        newSelection.removeAll(elements);
        currentSelectionProperty.set(newSelection);
    }

    public void deselectAll() {
        deselect(currentSelectionProperty.get());
    }

    public Set<PositionableViewModelElement<?>> getCurrentSelection() {
        return new HashSet<>(currentSelectionProperty.get());
    }

    public void updateSelections(Set<PositionableViewModelElement<?>> removedElements) {
        ArrayDeque<Set<PositionableViewModelElement<?>>> selectionToBeRemoved = new ArrayDeque<>();
        if (removedElements.stream().anyMatch(currentSelectionProperty.getValue()::contains)) {
            Set<PositionableViewModelElement<?>> newSelection = new HashSet<>(currentSelectionProperty.getValue());
            newSelection.removeAll(removedElements);
            currentSelectionProperty.set(newSelection);
            redoSelectionStack.clear();
        }
        Set<PositionableViewModelElement<?>> lastSelection = getCurrentSelection();
        for (Set<PositionableViewModelElement<?>> selection : undoSelectionStack) {
            if (selection.isEmpty()) {
                continue;
            }
            if (selection.removeAll(removedElements)) {
                redoSelectionStack.clear();
            }
            if (selection.isEmpty() || selection.equals(lastSelection)) {
                selectionToBeRemoved.push(selection);
            }
            lastSelection = selection;
        }
        this.undoSelectionStack.removeAll(selectionToBeRemoved);
    }

    public void updateSelections(PositionableViewModelElement<?> removedElement) {
        updateSelections(Set.of(removedElement));
    }
}
