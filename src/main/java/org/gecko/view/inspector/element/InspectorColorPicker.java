package org.gecko.view.inspector.element;

import javafx.beans.property.Property;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import org.gecko.view.inspector.element.InspectorElement;

public class InspectorColorPicker extends ColorPicker implements InspectorElement<ColorPicker> {
    public InspectorColorPicker(Property<Color> targetColorProperty) {
        valueProperty().bindBidirectional(targetColorProperty);
    }

    @Override
    public ColorPicker getControl() {
        return this;
    }
}
