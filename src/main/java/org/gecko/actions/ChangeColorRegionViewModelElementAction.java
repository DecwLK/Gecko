package org.gecko.actions;

import javafx.scene.paint.Color;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.RegionViewModel;

/**
 * A concrete representation of an {@link Action} that changes the color of a {@link RegionViewModel}. Additionally,
 * holds the old and new {@link Color}s of the region for undo/redo purposes.
 */
public class ChangeColorRegionViewModelElementAction extends Action {

    private final RegionViewModel regionViewModel;
    private final Color newColor;

    private final Color oldColor;

    ChangeColorRegionViewModelElementAction(RegionViewModel regionViewModel, Color color) {
        this.regionViewModel = regionViewModel;
        this.newColor = color;
        this.oldColor = regionViewModel.getColor();
    }

    @Override
    boolean run() throws GeckoException {
        regionViewModel.setColor(newColor);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangeColorRegionViewModelElementAction(regionViewModel, oldColor);
    }
}
