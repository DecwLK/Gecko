package org.gecko.actions;

import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

import java.util.List;

public class DeletePositionableViewModelElementAction extends Action {
    DeletePositionableViewModelElementAction(
            ActionFactory actionFactory,
            GeckoViewModel geckoViewModel,
            PositionableViewModelElement<?> element) {}

    DeletePositionableViewModelElementAction(
            ActionFactory actionFactory,
            GeckoViewModel geckoViewModel,
            List<PositionableViewModelElement<?>> element) {}

    @Override
    void run() {}

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
