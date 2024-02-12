package org.gecko.actions;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.SystemViewModel;

/**
 * A concrete representation of an {@link Action} that creates a {@link RegionViewModel} in the
 * current-{@link SystemViewModel} through the {@link org.gecko.viewmodel.ViewModelFactory} of the
 * {@link GeckoViewModel}. Additionally, holds the current {@link org.gecko.viewmodel.EditorViewModel EditorViewModel}
 * for setting the correct size and position for the created region.
 */
public class CreateRegionViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final Point2D position;
    private final Point2D size;
    private final Color color;
    private RegionViewModel createdRegionViewModel;

    CreateRegionViewModelElementAction(
        GeckoViewModel geckoViewModel, Point2D position, Point2D size, Color color) {
        this.geckoViewModel = geckoViewModel;
        this.position = position;
        this.size = size;
        this.color = color;
    }

    @Override
    boolean run() throws GeckoException {
        SystemViewModel currentParentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem();
        createdRegionViewModel = geckoViewModel.getViewModelFactory().createRegionViewModelIn(currentParentSystem);
        createdRegionViewModel.setPosition(position);
        createdRegionViewModel.setSize(size);
        if (color != null) {
            createdRegionViewModel.setColor(color);
        }
        createdRegionViewModel.updateTarget();
        ActionManager actionManager = geckoViewModel.getActionManager();
        actionManager.run(actionManager.getActionFactory().createSelectAction(createdRegionViewModel, true));
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(createdRegionViewModel);
    }
}
