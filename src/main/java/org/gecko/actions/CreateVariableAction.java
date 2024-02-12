package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;

/**
 * A concrete representation of an {@link Action} that creates a {@link PortViewModel} in the
 * current-{@link org.gecko.viewmodel.SystemViewModel SystemViewModel} through the
 * {@link org.gecko.viewmodel.ViewModelFactory ViewModelFactory} of the {@link GeckoViewModel}. Additionally, holds the
 * {@link Point2D position} and the current {@link EditorViewModel} for setting the correct position for the created
 * port.
 */
public class CreateVariableAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final EditorViewModel editorViewModel;
    private final Point2D position;
    private PortViewModel createdPortViewModel;

    CreateVariableAction(GeckoViewModel geckoViewModel, EditorViewModel editorViewModel, Point2D position) {
        this.geckoViewModel = geckoViewModel;
        this.editorViewModel = editorViewModel;
        this.position = position;
    }

    @Override
    boolean run() throws GeckoException {
        createdPortViewModel = geckoViewModel.getViewModelFactory()
            .createPortViewModelIn(geckoViewModel.getCurrentEditor().getCurrentSystem());
        createdPortViewModel.setCenter(editorViewModel.transformViewPortToWorldCoordinates(position));
        ActionManager actionManager = geckoViewModel.getActionManager();
        actionManager.run(actionManager.getActionFactory().createSelectAction(createdPortViewModel, true));
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(createdPortViewModel);
    }
}
