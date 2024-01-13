package org.gecko.view.inspector;

import javafx.scene.Node;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.viewmodel.PositionableViewModelElement;

import java.util.List;

public class Inspector<T extends PositionableViewModelElement<?>> {

    private T element;

    public Inspector(List<InspectorElement<?>> elements) {

    }

    public Node getView() {
        return null;
    }
}
