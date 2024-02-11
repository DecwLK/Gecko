package org.gecko.view.inspector.element;

import javafx.scene.control.ColorPicker;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.RegionViewModel;

/** Represents a type of {@link ColorPicker}, implementing the {@link InspectorElement} interface. Used for changing the color of a displayed {@link RegionViewModel}. */
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
