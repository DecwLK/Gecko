package org.gecko.view.inspector.element.list;

import javafx.scene.control.ListView;
import org.gecko.view.inspector.element.InspectorElement;

public abstract class AbstractInspectorList<T extends InspectorElement<?>> extends ListView<T> implements InspectorElement<ListView<T>> {

    @Override
    public ListView<T> getControl() {
        return this;
    }
}
