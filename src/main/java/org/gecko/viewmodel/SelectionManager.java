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
        redoSelectionStack.push(new HashSet<>(currentSelectionProperty.get()));
        currentSelectionProperty.set(undoSelectionStack.pop());
    }

    public void goForward() {
        undoSelectionStack.push(new HashSet<>(currentSelectionProperty.get()));
        currentSelectionProperty.set(redoSelectionStack.pop());
    }

    public void select(PositionableViewModelElement<?> element) {
        select(Set.of(element));
    }

    public void select(Set<PositionableViewModelElement<?>> elements) {
        redoSelectionStack.clear();
        undoSelectionStack.push(new HashSet<>(currentSelectionProperty.get()));
        currentSelectionProperty.set(elements);
    }

    public void deselect(PositionableViewModelElement<?> element) {
        deselect(Set.of(element));
    }

    public void deselect(Set<PositionableViewModelElement<?>> elements) {
        redoSelectionStack.clear();
        undoSelectionStack.push(new HashSet<>(currentSelectionProperty.get()));
        currentSelectionProperty.set(elements);
    }

    public void deselectAll() {
        deselect(currentSelectionProperty.get());
    }

    public Set<PositionableViewModelElement<?>> getCurrentSelection() {
        return new HashSet<>(currentSelectionProperty.get());
    }
}
