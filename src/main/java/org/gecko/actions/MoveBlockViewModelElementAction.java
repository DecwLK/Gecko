package org.gecko.actions;

import java.util.HashSet;
import java.util.Set;
import javafx.geometry.Point2D;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class MoveBlockViewModelElementAction extends Action {

    private final EditorViewModel editorViewModel;
    private Set<PositionableViewModelElement<?>> elementsToMove;
    private final Point2D startPosition;
    private final Point2D endPosition;

    MoveBlockViewModelElementAction(EditorViewModel editorViewModel, Point2D startPosition, Point2D endPosition) {
        this.editorViewModel = editorViewModel;
        this.elementsToMove = null;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    MoveBlockViewModelElementAction(
        EditorViewModel editorViewModel, Set<PositionableViewModelElement<?>> elementsToMove, Point2D startPosition,
        Point2D endPosition) {
        this.editorViewModel = editorViewModel;
        this.elementsToMove = elementsToMove;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    @Override
    void run() {
        if (elementsToMove == null) {
            elementsToMove = new HashSet<>(editorViewModel.getSelectionManager().getCurrentSelection());
        }
        for (PositionableViewModelElement<?> element : elementsToMove) {
            element.setPosition(editorViewModel.transformScreenToWorldCoordinates(endPosition));
        }
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createMoveBlockViewModelElementAction(elementsToMove, endPosition, startPosition);
    }
}
