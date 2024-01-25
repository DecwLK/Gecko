package org.gecko.actions;

import org.gecko.viewmodel.BlockViewModelElement;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.Renamable;

public class RenameViewModelElementAction extends Action {
    private final GeckoViewModel geckoViewModel;
    private final Renamable renamable;
    private final String newName;

    RenameViewModelElementAction(GeckoViewModel geckoViewModel, Renamable renamable, String newName) {
        this.geckoViewModel = geckoViewModel;
        this.renamable = renamable;
        this.newName = newName;
    }

    @Override
    void run() {
        BlockViewModelElement<?> elementToRename = null;
        try {
            elementToRename = (BlockViewModelElement<?>) this.renamable;
        } catch (ClassCastException e) {
            // Program shouldn't get here because the only renamable positionable elements are the block ones.
            // TODO: Handle parameter type by e.g. multiple constructors.
            //  TODO: Contracts should also have access to this Action.
        }

        ((BlockViewModelElement<?>) this.geckoViewModel.getViewModelElement(elementToRename.getTarget())).setName(
            this.newName);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
