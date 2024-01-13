package org.gecko.actions;

import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class PastePositionableViewModelElementAction extends Action {
    PastePositionableViewModelElementAction(
            ActionFactory actionFactory, GeckoViewModel geckoViewModel) {}

    PastePositionableViewModelElementAction(
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
