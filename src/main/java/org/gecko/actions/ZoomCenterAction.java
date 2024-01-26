package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.viewmodel.EditorViewModel;

public class ZoomCenterAction extends Action {

    private final EditorViewModel editorViewModel;
    private final double factor;

    ZoomCenterAction(EditorViewModel editorViewModel, double factor) {
        this.editorViewModel = editorViewModel;
        this.factor = factor;
    }

    @Override
    void run() {
        editorViewModel.zoomCenter(factor);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
