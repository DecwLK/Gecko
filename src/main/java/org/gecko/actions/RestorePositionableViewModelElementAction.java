package org.gecko.actions;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

/** A concrete representation of an {@link Action} that restores a set of deleted {@link PositionableViewModelElement}s in the current {@link org.gecko.viewmodel.EditorViewModel}. */
public class RestorePositionableViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final ActionGroup actionGroup;
    private final Set<PositionableViewModelElement<?>> deletedElements;

    RestorePositionableViewModelElementAction(
        GeckoViewModel geckoViewModel, ActionGroup actionGroup, Set<PositionableViewModelElement<?>> deletedElements) {
        this.geckoViewModel = geckoViewModel;
        this.actionGroup = actionGroup;
        this.deletedElements = new HashSet<>(deletedElements);
    }

    @Override
    boolean run() throws GeckoException {
        Action undoAction = actionGroup.getUndoAction(geckoViewModel.getActionManager().getActionFactory());
        if (!undoAction.run()) {
            return false;
        }
        Set<PositionableViewModelElement<?>> elementsToRestoreFromCurrentEditor = geckoViewModel.getCurrentEditor()
            .getContainedPositionableViewModelElementsProperty()
            .stream()
            .filter(deletedElements::contains)
            .collect(Collectors.toSet());
        ActionManager actionManager = geckoViewModel.getActionManager();
        actionManager.run(
            actionManager.getActionFactory().createSelectAction(elementsToRestoreFromCurrentEditor, true));
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(deletedElements);
    }
}
