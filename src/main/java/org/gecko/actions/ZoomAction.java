package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.EditorViewModel;

/** A concrete representation of an {@link Action} that zooms in or out in the given {@link EditorViewModel} from a given {link Point2D pivot} by a given zoom factor. */
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
    boolean run() throws GeckoException {
        editorViewModel.zoom(pivot, factor);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
