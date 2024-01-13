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
        //TODO stub
    }
    public void goForward() {
        //TODO stub
    }
    public void select(PositionableViewModelElement<?> element) {
        //TODO stub
    }
    public void select(List<PositionableViewModelElement<?>> elements) {
        //TODO stub
    }
    public void deselect(PositionableViewModelElement<?> element) {
        //TODO stub
    }
    public void deselect(List<PositionableViewModelElement<?>> elements) {
        //TODO stub
    }
    public void deselectAll() {
        //TODO stub
    }
}
