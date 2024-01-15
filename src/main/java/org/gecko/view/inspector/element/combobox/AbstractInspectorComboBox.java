package org.gecko.view.inspector.element.combobox;

import javafx.scene.control.ComboBox;
import org.gecko.view.inspector.element.InspectorElement;

public abstract class AbstractInspectorComboBox<T extends InspectorElement<?>> extends ComboBox<T> implements InspectorElement<ComboBox<?>> {

    @Override
    public ComboBox<T> getControl() {
        return this;
    }
}
