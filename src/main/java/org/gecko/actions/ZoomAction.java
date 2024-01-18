package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.viewmodel.EditorViewModel;

public class ZoomAction extends Action {

    private final EditorViewModel editorViewModel;
    private final Point2D pivot;
    private final double factor;

    ZoomAction(EditorViewModel editorViewModel, Point2D pivot, double factor) {
        this.editorViewModel = editorViewModel;
        this.pivot = pivot;
        this.factor = factor;
    }

    @Override
    void run() {
        editorViewModel.zoomIn(pivot, factor);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
