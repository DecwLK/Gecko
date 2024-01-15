package org.gecko.view.inspector.element.textfield;

import javafx.beans.property.IntegerProperty;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

import org.gecko.view.inspector.element.InspectorElement;

public class InspectorPriorityField extends TextField implements InspectorElement<TextField> {

    public InspectorPriorityField(IntegerProperty targetProperty) {
        textProperty().bindBidirectional(targetProperty, new NumberStringConverter());
    }

    @Override
    public TextField getControl() {
        return this;
    }
}
