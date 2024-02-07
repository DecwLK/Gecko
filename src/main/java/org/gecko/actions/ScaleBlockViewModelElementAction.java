package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.BlockViewModelElement;

public class ScaleBlockViewModelElementAction extends Action {
    ScaleBlockViewModelElementAction(BlockViewModelElement<?> element, double scaleFactor) {
    }

    @Override
    boolean run() throws GeckoException {
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
