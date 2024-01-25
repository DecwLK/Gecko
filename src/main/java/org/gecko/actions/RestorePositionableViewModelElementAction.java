package org.gecko.actions;

import java.util.List;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class RestorePositionableViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final List<PositionableViewModelElement<?>> elementsToDelete;

    RestorePositionableViewModelElementAction(
        GeckoViewModel geckoViewModel, List<PositionableViewModelElement<?>> elements) {
        this.geckoViewModel = geckoViewModel;
        this.elementsToDelete = elements;
    }

    @Override
    void run() {
        for (PositionableViewModelElement<?> element : elementsToDelete) {
            geckoViewModel.restoreViewModelElement(element);
        }
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(elementsToDelete);
    }
}
