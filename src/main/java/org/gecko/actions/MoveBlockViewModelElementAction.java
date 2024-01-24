package org.gecko.actions;

import java.util.Set;
import javafx.geometry.Point2D;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class MoveBlockViewModelElementAction extends Action {

    private final EditorViewModel editorViewModel;
    private final Set<PositionableViewModelElement<?>> elementsToMove;
    private final PositionableViewModelElement<?> pivotElement;
    private final Point2D newPivotPosition;

    private final Point2D oldPivotPosition;

    MoveBlockViewModelElementAction(EditorViewModel editorViewModel, Point2D newPivotPosition) {
        this.editorViewModel = editorViewModel;
        this.elementsToMove = editorViewModel.getSelectionManager().getCurrentSelection();
        this.pivotElement = elementsToMove.iterator().next();
        this.newPivotPosition = newPivotPosition;
        this.oldPivotPosition = pivotElement.getCenter();
    }

    MoveBlockViewModelElementAction(EditorViewModel editorViewModel, Set<PositionableViewModelElement<?>> elementsToMove, Point2D newPivotPosition) {
        this.editorViewModel = editorViewModel;
        this.elementsToMove = elementsToMove;
        this.pivotElement = elementsToMove.iterator().next();
        this.newPivotPosition = newPivotPosition;
        this.oldPivotPosition = pivotElement.getCenter();
    }

    @Override
    void run() {
        // Calculate position delta by moving the pivot element
        Point2D prevPosition = pivotElement.getCenter();
        pivotElement.setCenter(editorViewModel.transformScreenToWorldCoordinates(newPivotPosition));
        Point2D positionDelta = pivotElement.getCenter().subtract(prevPosition);

        // Move all elements by the same delta
        for (PositionableViewModelElement<?> element : elementsToMove) {
            if (element == pivotElement) {
                continue;
            }
            element.setCenter(element.getCenter().add(positionDelta));
        }
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createMoveBlockViewModelElementAction(elementsToMove, oldPivotPosition);
    }
}
