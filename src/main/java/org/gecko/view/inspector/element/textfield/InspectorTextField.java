package org.gecko.view.inspector.element.textfield;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorElement;

/**
 * A concrete representation of an {@link TextField} implementing the {@link InspectorElement} interface, which
 * encapsulates a {@link TextField}.
 */
public abstract class InspectorTextField extends TextField implements InspectorElement<TextField> {

    protected InspectorTextField(StringProperty stringProperty, ActionManager actionManager) {
        setText(stringProperty.get());
        stringProperty.addListener((observable, oldValue, newValue) -> setText(newValue));
        setOnAction(event -> {
            if (getText().isEmpty()) {
                setText(stringProperty.get());
            }
            getParent().requestFocus();
            if (getText().equals(stringProperty.get())) {
                return;
            }
            actionManager.run(getAction());
            setText(stringProperty.get());
        });
    }

    protected abstract Action getAction();

    @Override
    public TextField getControl() {
        return this;
    }
}
