package org.gecko.view.inspector.element;

import javafx.scene.Node;

/** Represents a generic interface that encapsulates a subtype of {@link Node} displayed in an {@link org.gecko.view.inspector.Inspector Inspector}. The provided method must be implemented by concrete inspector elements. */
public interface InspectorElement<T extends Node> {
    T getControl();
}
