package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;

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
        createdStateViewModel.setCenter(editorViewModel.transformScreenToWorldCoordinates(position));
        ActionManager actionManager = geckoViewModel.getActionManager();
        actionManager.run(actionManager.getActionFactory().createSelectAction(createdStateViewModel, true));
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(createdStateViewModel);
    }
}
