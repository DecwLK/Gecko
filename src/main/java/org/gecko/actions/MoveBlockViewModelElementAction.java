package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.viewmodel.SelectionManager;

public class MoveBlockViewModelElementAction extends Action {

    private final SelectionManager selectionManager;
    private final Point2D offset;

    MoveBlockViewModelElementAction(SelectionManager selectionManager, Point2D offset) {
        this.selectionManager = selectionManager;
        this.offset = offset;
    }

    @Override
    void run() {
        selectionManager.getCurrentSelection().forEach(element -> {
            element.getPosition().add(offset);
        });
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createMoveBlockViewModelElementAction(offset.multiply(-1));
    }
}
