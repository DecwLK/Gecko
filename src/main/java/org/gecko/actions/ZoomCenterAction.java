package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.EditorViewModel;

public class ZoomCenterAction extends Action {

    private final EditorViewModel editorViewModel;
    private final double factor;

    ZoomCenterAction(EditorViewModel editorViewModel, double factor) {
        this.editorViewModel = editorViewModel;
        this.factor = factor;
    }

    @Override
    boolean run() throws GeckoException {
        editorViewModel.zoomCenter(factor);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
