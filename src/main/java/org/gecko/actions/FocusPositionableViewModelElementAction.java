package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class FocusPositionableViewModelElementAction extends Action {

    private final EditorViewModel editorViewModel;
    private final PositionableViewModelElement<?> element;

    public FocusPositionableViewModelElementAction(EditorViewModel editorViewModel, PositionableViewModelElement<?> positionableViewModelElement) {
        this.editorViewModel = editorViewModel;
        this.element = positionableViewModelElement;
    }

    @Override
    void run() {
        editorViewModel.getSelectionManager().select(element);
        double newX = (element.getSize().getX() - element.getPosition().getX()) / 2;
        double newY = (element.getSize().getY() - element.getPosition().getY()) / 2;
        editorViewModel.setPivot(new Point2D(newX, newY));
        editorViewModel.setFocusedElement(element);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
