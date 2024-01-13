package org.gecko.view.inspector.element.container;

import javafx.scene.layout.VBox;
import org.gecko.view.inspector.element.InspectorElement;

import java.util.List;

public abstract class InspectorContainerItem extends VBox
        implements InspectorElement<VBox> {

    InspectorContainerItem(List<InspectorElement<?>> elements) {
        for (InspectorElement<?> element : elements) {
            getChildren().add(element.getControl());
        }
    }

    @Override
    public VBox getControl() {
        return this;
    }
}
