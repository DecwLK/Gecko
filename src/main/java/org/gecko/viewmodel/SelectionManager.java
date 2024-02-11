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

    /**
     * Goes back to the previous selection.
     */
    public void goBack() {
        if (undoSelectionStack.isEmpty()) {
            return;
        }
        redoSelectionStack.push(new HashSet<>(currentSelectionProperty.get()));
        currentSelectionProperty.set(undoSelectionStack.pop());
    }

    /**
     * Goes forward to the next selection.
     */
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

    /**
     * Selects the given elements. If the given elements are already selected, nothing happens. A new selection is made
     * with the given elements. It works by setting the current selection to the given elements and pushing the current
     * selection to the undo stack. The redo stack is cleared because a new selection was made.
     *
     * @param elements the elements to be selected
     */
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

    /**
     * Deselects the given elements. A new selection is made with the remaining elements. If the given elements are not
     * selected, nothing happens.
     *
     * @param elements the elements to be deselected
     */
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

    /**
     * Updates all selections by removing the given elements from them. This method is used when elements are removed
     * from the view model. It keeps the selections consistent.
     *
     * @param removedElements the elements that are removed from the view model
     */
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
