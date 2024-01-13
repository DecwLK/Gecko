package org.gecko.actions;

import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

import java.util.List;

public class RestorePositionableViewModelElementAction extends Action {
    RestorePositionableViewModelElementAction(
            ActionFactory actionFactory,
            GeckoViewModel geckoViewModel,
            List<PositionableViewModelElement<?>> elements) {}

    @Override
    void run() {}

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
