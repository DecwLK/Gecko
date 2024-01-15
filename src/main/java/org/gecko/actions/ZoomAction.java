package org.gecko.actions;

import javafx.geometry.Point2D;

import org.gecko.viewmodel.EditorViewModel;

public class ZoomAction extends Action {

    ZoomAction(EditorViewModel editorViewModel, Point2D pivot, double factor) {
    }

    @Override
    void run() {
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
