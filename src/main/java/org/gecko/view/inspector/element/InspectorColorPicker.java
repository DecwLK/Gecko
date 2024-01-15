package org.gecko.view.inspector.element;

import javafx.scene.control.ColorPicker;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.RegionViewModel;

public class InspectorColorPicker extends ColorPicker implements InspectorElement<ColorPicker> {
    public InspectorColorPicker(ActionManager actionManager, RegionViewModel regionViewModel) {
        setValue(regionViewModel.getColor());

        valueProperty().addListener((observable, oldValue, newValue) -> {
            actionManager.run(actionManager.getActionFactory().createChangeColorRegionViewModelElementAction(regionViewModel, newValue));
        });
    }

    @Override
    public ColorPicker getControl() {
        return this;
    }
}
