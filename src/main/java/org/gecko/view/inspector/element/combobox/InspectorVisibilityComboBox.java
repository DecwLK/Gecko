package org.gecko.view.inspector.element.combobox;

import javafx.beans.property.Property;
import javafx.scene.control.ComboBox;
import org.gecko.model.Visibility;
import org.gecko.view.inspector.element.InspectorElement;

public class InspectorVisibilityComboBox extends ComboBox<Visibility> implements InspectorElement<ComboBox<Visibility>> {
    public InspectorVisibilityComboBox(Property<Visibility> visibilityProperty) {
        getItems().setAll(Visibility.values());
        valueProperty().bindBidirectional(visibilityProperty);
    }

    @Override
    public ComboBox<Visibility> getControl() {
        return this;
    }
}
