package org.gecko.view.inspector.element;

import javafx.scene.Node;

public interface InspectorElement<T extends Node> {
    T getControl();
}
