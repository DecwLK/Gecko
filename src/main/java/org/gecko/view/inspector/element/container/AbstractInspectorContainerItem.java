package org.gecko.view.inspector.element.container;

import javafx.scene.layout.VBox;
import org.gecko.view.inspector.element.InspectorElement;

public abstract class AbstractInspectorContainerItem extends VBox implements InspectorElement<VBox> {

    @Override
    public VBox getControl() {
        return this;
    }
}
