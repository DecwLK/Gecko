package org.gecko.actions;

import java.util.List;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class PastePositionableViewModelElementAction extends Action {
    PastePositionableViewModelElementAction(GeckoViewModel geckoViewModel) {
    }

    PastePositionableViewModelElementAction(
        GeckoViewModel geckoViewModel, List<PositionableViewModelElement<?>> elements) {
    }

    @Override
    void run() {
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
