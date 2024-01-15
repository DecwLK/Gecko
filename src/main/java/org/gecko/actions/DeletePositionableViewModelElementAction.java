package org.gecko.actions;

import java.util.List;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class DeletePositionableViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final List<PositionableViewModelElement<?>> elementsToDelete;

    DeletePositionableViewModelElementAction(GeckoViewModel geckoViewModel, PositionableViewModelElement<?> element) {
        this.geckoViewModel = geckoViewModel;
        this.elementsToDelete = List.of(element);
    }

    DeletePositionableViewModelElementAction(GeckoViewModel geckoViewModel, List<PositionableViewModelElement<?>> elements) {
        this.geckoViewModel = geckoViewModel;
        this.elementsToDelete = elements;
    }

    @Override
    void run() {
        for (PositionableViewModelElement<?> element : elementsToDelete) {
            geckoViewModel.deleteViewModelElement(element);
        }
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createRestorePositionableViewModelElementAction(elementsToDelete);
    }
}
