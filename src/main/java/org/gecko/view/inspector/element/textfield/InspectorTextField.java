package org.gecko.view.inspector.element.textfield;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import org.gecko.view.inspector.element.InspectorElement;

public class InspectorTextField extends TextField
        implements InspectorElement<TextField> {

    public InspectorTextField(IntegerProperty targetIntegerProperty) {
        textProperty().bindBidirectional(targetIntegerProperty, new NumberStringConverter());
    }

    public InspectorTextField(StringProperty targetStringProperty) {
        textProperty().bindBidirectional(targetStringProperty);
    }

    @Override
    public TextField getControl() {
        return this;
    }
}
