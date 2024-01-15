package org.gecko.actions;

import java.util.List;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class RestorePositionableViewModelElementAction extends Action {
    RestorePositionableViewModelElementAction(GeckoViewModel geckoViewModel, List<PositionableViewModelElement<?>> elements) {
    }

    @Override
    void run() {
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
