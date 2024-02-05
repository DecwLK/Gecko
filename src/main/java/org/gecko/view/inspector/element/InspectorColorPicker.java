package org.gecko.view.inspector.element;

import javafx.scene.control.ColorPicker;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.RegionViewModel;

public class InspectorColorPicker extends ColorPicker implements InspectorElement<ColorPicker> {
    public InspectorColorPicker(ActionManager actionManager, RegionViewModel regionViewModel) {
        setValue(regionViewModel.getColor());
        regionViewModel.getColorProperty().addListener((observable, oldValue, newValue) -> setValue(newValue));

        setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory()
                .createChangeColorRegionViewModelElementAction(regionViewModel, getValue()));
        });
    }

    @Override
    public ColorPicker getControl() {
        return this;
    }
}
