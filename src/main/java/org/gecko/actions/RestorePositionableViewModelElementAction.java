package org.gecko.actions;

import java.util.HashSet;
import java.util.Set;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class RestorePositionableViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final Set<PositionableViewModelElement<?>> elementsToDelete;

    RestorePositionableViewModelElementAction(
        GeckoViewModel geckoViewModel, Set<PositionableViewModelElement<?>> elements) {
        this.geckoViewModel = geckoViewModel;
        this.elementsToDelete = new HashSet<>(elements);
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
