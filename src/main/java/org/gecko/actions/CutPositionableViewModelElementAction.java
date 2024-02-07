package org.gecko.actions;

import java.util.List;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class CutPositionableViewModelElementAction extends Action {
    CutPositionableViewModelElementAction(
        EditorViewModel editorViewModel, List<PositionableViewModelElement<?>> elements) {
    }

    @Override
    boolean run() throws GeckoException {
        // TODO: copy + delete Actions
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
