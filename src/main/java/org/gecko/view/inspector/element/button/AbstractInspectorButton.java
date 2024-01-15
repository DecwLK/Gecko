package org.gecko.view.inspector.element.button;

import javafx.scene.control.Button;

import org.gecko.view.inspector.element.InspectorElement;

public abstract class AbstractInspectorButton extends Button implements InspectorElement<Button> {
    @Override
    public Button getControl() {
        return this;
    }
}
