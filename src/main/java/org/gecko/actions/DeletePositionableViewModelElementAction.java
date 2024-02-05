package org.gecko.actions;

import java.util.HashSet;
import java.util.Set;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class DeletePositionableViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final Set<PositionableViewModelElement<?>> elementsToDelete;

    DeletePositionableViewModelElementAction(GeckoViewModel geckoViewModel, PositionableViewModelElement<?> element) {
        this.geckoViewModel = geckoViewModel;
        this.elementsToDelete = Set.of(element);
    }

    DeletePositionableViewModelElementAction(
        GeckoViewModel geckoViewModel, Set<PositionableViewModelElement<?>> elements) {
        this.geckoViewModel = geckoViewModel;
        this.elementsToDelete = new HashSet<>(elements);
    }

    @Override
    void run() {
        //SelectionManager selectionManager = geckoViewModel.getCurrentEditor().getSelectionManager();
        // Find all dependencies
        /*PositionableViewModelElementVisitor visitor = new ViewModelElementDependencyFinderVisitor(geckoViewModel,
            geckoViewModel.getCurrentEditor().getCurrentSystem());

        for (PositionableViewModelElement<?> element : elementsToDelete) {
            for (PositionableViewModelElement<?> dependency : (Set<PositionableViewModelElement<?>>) element.accept(
                visitor)) {
                selectionManager.deselect(dependency);
                selectionManager.updateSelections(dependency);
                geckoViewModel.deleteViewModelElement(dependency);
            }
            selectionManager.deselect(element);
            selectionManager.updateSelections(element);
            geckoViewModel.deleteViewModelElement(element);
        }*/
        elementsToDelete.forEach(geckoViewModel::deleteViewModelElement);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createRestorePositionableViewModelElementAction(elementsToDelete);
    }
}
