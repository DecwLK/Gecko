package org.gecko.view.inspector.element;

import javafx.scene.control.Separator;

public class InspectorSeparator extends Separator implements InspectorElement<Separator> {

    @Override
    public Separator getControl() {
        return this;
    }
}
