package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.viewmodel.SelectionManager;

public class MoveBlockViewModelElementAction extends Action {
    MoveBlockViewModelElementAction(SelectionManager selectionManager, Point2D offset) {}

    @Override
    void run() {}

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
