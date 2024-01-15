package org.gecko.view.inspector.element.label;

import javafx.scene.control.Label;
import org.gecko.view.inspector.element.InspectorElement;

public class InspectorLabel extends Label implements InspectorElement<Label> {

    public InspectorLabel(String text) {
        super(text);
    }

    @Override
    public Label getControl() {
        return this;
    }
}
