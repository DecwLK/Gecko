package org.gecko.view.inspector.element.textfield;

import javafx.scene.control.TextArea;
import org.gecko.view.inspector.element.InspectorElement;

public abstract class InspectorContractField extends TextArea implements InspectorElement<TextArea> {
    private static final int MAX_HEIGHT = 30;
    private static final int EXPANDED_MAX_HEIGHT = 90;

    protected InspectorContractField() {
        setPrefHeight(MAX_HEIGHT);
        setWrapText(true);
    }

    public void toggleExpand() {
        setPrefHeight(getPrefHeight() == MAX_HEIGHT ? EXPANDED_MAX_HEIGHT : MAX_HEIGHT);
    }
}
