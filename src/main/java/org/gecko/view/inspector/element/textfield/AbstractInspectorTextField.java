package org.gecko.view.inspector.element.textfield;

import javafx.scene.control.TextField;
import org.gecko.view.inspector.element.InspectorElement;

public abstract class AbstractInspectorTextField extends TextField implements InspectorElement<TextField> {

    @Override
    public TextField getControl() {
        return this;
    }
}
