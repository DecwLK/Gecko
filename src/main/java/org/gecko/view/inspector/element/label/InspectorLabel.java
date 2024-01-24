package org.gecko.view.inspector.element.label;

import javafx.scene.control.Label;
import org.gecko.view.inspector.element.InspectorElement;

public class InspectorLabel extends Label implements InspectorElement<Label> {
    private static final String STYLE_NAME = "inspector-label";

    public InspectorLabel(String text) {
        super(text);
        getStyleClass().add(STYLE_NAME);
    }

    @Override
    public Label getControl() {
        return this;
    }
}
