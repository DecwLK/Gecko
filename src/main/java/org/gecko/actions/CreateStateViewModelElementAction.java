package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;

/**
 * A concrete representation of an {@link Action} that creates a {@link StateViewModel} in the
 * current-{@link SystemViewModel} through the {@link org.gecko.viewmodel.ViewModelFactory ViewModelFactory} of the
 * {@link GeckoViewModel}. Additionally, holds the {@link Point2D position} and the current {@link EditorViewModel} for
 * setting the correct position for the created state.
 */
public class CreateStateViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final EditorViewModel editorViewModel;
    private final Point2D position;
    private StateViewModel createdStateViewModel;

    CreateStateViewModelElementAction(
        GeckoViewModel geckoViewModel, EditorViewModel editorViewModel, Point2D position) {
        this.geckoViewModel = geckoViewModel;
        this.editorViewModel = editorViewModel;
        this.position = position;
    }

    @Override
    boolean run() throws GeckoException {
        SystemViewModel currentParentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem();
        createdStateViewModel = geckoViewModel.getViewModelFactory().createStateViewModelIn(currentParentSystem);
        createdStateViewModel.setCenter(position);
        editorViewModel.updateRegions();
        ActionManager actionManager = geckoViewModel.getActionManager();
        actionManager.run(actionManager.getActionFactory().createSelectAction(createdStateViewModel, true));
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(createdStateViewModel);
    }
}
