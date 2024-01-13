package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.viewmodel.EditorViewModel;

public class PanAction extends Action {

    PanAction(ActionFactory actionFactory, EditorViewModel editorViewModel, Point2D offset) {
    }

    @Override
    void run() {

    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
