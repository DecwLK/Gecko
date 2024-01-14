package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.viewmodel.EditorViewModel;

public class PanAction extends Action {

    private final EditorViewModel editorViewModel;
    private final Point2D offset;

    PanAction(EditorViewModel editorViewModel, Point2D offset) {
        this.editorViewModel = editorViewModel;
        this.offset = offset;
    }

    @Override
    void run() {
        editorViewModel.setPivot(editorViewModel.getPivot().add(offset));
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createPanAction(offset.multiply(-1));
    }
}
