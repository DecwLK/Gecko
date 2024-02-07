package org.gecko.actions;

import javafx.scene.paint.Color;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.RegionViewModel;

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
