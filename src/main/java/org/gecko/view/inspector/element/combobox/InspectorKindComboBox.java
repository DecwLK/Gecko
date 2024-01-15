package org.gecko.view.inspector.element.combobox;

import javafx.beans.property.Property;
import javafx.scene.control.ComboBox;
import org.gecko.model.Kind;
import org.gecko.view.inspector.element.InspectorElement;

public class InspectorKindComboBox extends ComboBox<Kind> implements InspectorElement<ComboBox<Kind>> {
    public InspectorKindComboBox(Property<Kind> kindProperty) {
        getItems().setAll(Kind.values());
        valueProperty().bindBidirectional(kindProperty);
    }

    @Override
    public ComboBox<Kind> getControl() {
        return this;
    }
}
