package org.gecko.view.inspector.element.label;

import javafx.scene.control.Label;
import org.gecko.view.inspector.element.InspectorElement;

public abstract class AbstractInspectorLabel extends Label implements InspectorElement<Label> {

    @Override
    public Label getControl() {
        return this;
    }
}
