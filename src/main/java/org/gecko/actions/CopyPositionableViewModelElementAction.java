package org.gecko.actions;

import java.util.List;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class CopyPositionableViewModelElementAction extends Action {
    CopyPositionableViewModelElementAction(
        EditorViewModel editorViewModel, List<PositionableViewModelElement<?>> positionableViewModelElement) {
    }

    @Override
    boolean run() throws GeckoException {
        //TODO how does copy work?
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
