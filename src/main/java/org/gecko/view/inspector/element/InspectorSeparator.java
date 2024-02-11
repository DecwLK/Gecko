package org.gecko.view.inspector.element;

import javafx.scene.control.Separator;

/** Represents a type of {@link Separator}, implementing the {@link InspectorElement} interface. Serves as delimiter between other inspector elements. */
public class InspectorSeparator extends Separator implements InspectorElement<Separator> {

    @Override
    public Separator getControl() {
        return this;
    }
}
