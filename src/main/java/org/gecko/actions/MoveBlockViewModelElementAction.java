package org.gecko.actions;

import java.util.HashSet;
import java.util.Set;
import javafx.geometry.Point2D;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

/**
 * A concrete representation of an {@link Action} that moves a set of {link PositionableViewModelElement}s with a given
 * {@link Point2D delta value}.
 */
public class MoveBlockViewModelElementAction extends Action {

    private final EditorViewModel editorViewModel;
    private Set<PositionableViewModelElement<?>> elementsToMove;
    private final Point2D delta;

    MoveBlockViewModelElementAction(EditorViewModel editorViewModel, Point2D delta) {
        this.editorViewModel = editorViewModel;
        this.elementsToMove = null;
        this.delta = delta;
    }

    MoveBlockViewModelElementAction(
        EditorViewModel editorViewModel, Set<PositionableViewModelElement<?>> elementsToMove, Point2D delta) {
        this.editorViewModel = editorViewModel;
        this.elementsToMove = elementsToMove;
        this.delta = delta;
    }

    @Override
    boolean run() throws GeckoException {
        if (delta.equals(Point2D.ZERO)) {
            return false;
        }
        if (elementsToMove == null) {
            elementsToMove = new HashSet<>(editorViewModel.getSelectionManager().getCurrentSelection());
        }
        for (PositionableViewModelElement<?> element : elementsToMove) {
            element.setPosition(element.getPosition().add(delta));
        }

        editorViewModel.updateRegions();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createMoveBlockViewModelElementAction(elementsToMove, delta.multiply(-1));
    }
}
