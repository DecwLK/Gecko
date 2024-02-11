package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class CreateSystemViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final EditorViewModel editorViewModel;
    private final Point2D position;
    private SystemViewModel createdSystemViewModel;

    CreateSystemViewModelElementAction(
        GeckoViewModel geckoViewModel, EditorViewModel editorViewModel, Point2D position) {
        this.geckoViewModel = geckoViewModel;
        this.editorViewModel = editorViewModel;
        this.position = position;
    }

    @Override
    boolean run() throws GeckoException {
        SystemViewModel currentParentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem();
        createdSystemViewModel = geckoViewModel.getViewModelFactory().createSystemViewModelIn(currentParentSystem);
        Point2D pos = editorViewModel.transformViewPortToWorldCoordinates(position);
        createdSystemViewModel.setCenter(pos);
        ActionManager actionManager = geckoViewModel.getActionManager();
        actionManager.run(actionManager.getActionFactory().createSelectAction(createdSystemViewModel, true));
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(createdSystemViewModel);
    }
}