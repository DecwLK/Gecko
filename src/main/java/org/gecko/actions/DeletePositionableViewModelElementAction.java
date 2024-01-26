package org.gecko.actions;

import java.util.HashSet;
import java.util.Set;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.PositionableViewModelElementVisitor;
import org.gecko.viewmodel.ViewModelElementDependencyFinderVisitor;

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
        // Find all dependencies
        PositionableViewModelElementVisitor visitor = new ViewModelElementDependencyFinderVisitor(geckoViewModel,
            geckoViewModel.getCurrentEditor().getCurrentSystem());

        for (PositionableViewModelElement<?> element : elementsToDelete) {
            for (PositionableViewModelElement<?> dependency : (Set<PositionableViewModelElement<?>>) element.accept(
                visitor)) {
                geckoViewModel.deleteViewModelElement(dependency);
            }
            geckoViewModel.deleteViewModelElement(element);
        }
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createRestorePositionableViewModelElementAction(elementsToDelete);
    }
}
