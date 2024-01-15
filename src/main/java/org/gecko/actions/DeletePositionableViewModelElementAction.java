package org.gecko.actions;

import java.util.List;

import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class DeletePositionableViewModelElementAction extends Action {
    DeletePositionableViewModelElementAction(GeckoViewModel geckoViewModel, PositionableViewModelElement<?> element) {
    }

    DeletePositionableViewModelElementAction(GeckoViewModel geckoViewModel, List<PositionableViewModelElement<?>> elements) {
    }

    @Override
    void run() {
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
