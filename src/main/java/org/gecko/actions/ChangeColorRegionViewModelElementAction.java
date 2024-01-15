package org.gecko.actions;

import javafx.scene.paint.Color;
import org.gecko.viewmodel.RegionViewModel;

public class ChangeColorRegionViewModelElementAction extends Action {

    private final RegionViewModel regionViewModel;
    private final Color newColor;

    private final Color oldColor;

    public ChangeColorRegionViewModelElementAction(RegionViewModel regionViewModel, Color color) {
        this.regionViewModel = regionViewModel;
        this.newColor = color;

        this.oldColor = regionViewModel.getColor();
    }

    @Override
    void run() {
        regionViewModel.setColor(newColor);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangeColorRegionViewModelElementAction(regionViewModel, oldColor);
    }
}
