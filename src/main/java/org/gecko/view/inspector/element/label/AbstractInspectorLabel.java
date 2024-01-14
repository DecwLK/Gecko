package org.gecko.view.inspector.element.label;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.scene.control.Label;
import org.gecko.view.inspector.element.InspectorElement;

public abstract class AbstractInspectorLabel extends Label implements InspectorElement<Label> {

    protected AbstractInspectorLabel(Property<String> targetStringProperty) {
        Bindings.bindBidirectional(textProperty(), targetStringProperty);
    }

    @Override
    public Label getControl() {
        return this;
    }
}
