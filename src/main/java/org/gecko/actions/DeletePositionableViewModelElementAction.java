package org.gecko.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

/** A concrete representation of an {@link Action} that deletes a set of {@link PositionableViewModelElement}s and their dependencies from the {@link GeckoViewModel}. */
public class DeletePositionableViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final Set<PositionableViewModelElement<?>> elementsToDelete;
    private ActionGroup deleteActionGroup;
    private Set<PositionableViewModelElement<?>> deletedElements;

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
    boolean run() throws GeckoException {
        Set<AbstractPositionableViewModelElementAction> allDeleteActions = new HashSet<>();
        for (PositionableViewModelElement<?> element : elementsToDelete) {
            DeleteActionsCreatorVisitor visitor =
                new DeleteActionsCreatorVisitor(geckoViewModel, geckoViewModel.getCurrentEditor().getCurrentSystem());
            Set<AbstractPositionableViewModelElementAction> foundDeleteActions;
            try {
                foundDeleteActions = (Set<AbstractPositionableViewModelElementAction>) element.accept(visitor);
            } catch (ClassCastException e) {
                throw new GeckoException("Error while deleting element");
            }
            allDeleteActions.addAll(foundDeleteActions);
        }

        deletedElements = allDeleteActions.stream()
            .map(AbstractPositionableViewModelElementAction::getTarget)
            .collect(Collectors.toSet());
        geckoViewModel.getCurrentEditor().getSelectionManager().deselect(deletedElements);
        deleteActionGroup = new ActionGroup(new ArrayList<>(allDeleteActions));
        return deleteActionGroup.run();
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return new RestorePositionableViewModelElementAction(geckoViewModel, deleteActionGroup, deletedElements);
    }
}
