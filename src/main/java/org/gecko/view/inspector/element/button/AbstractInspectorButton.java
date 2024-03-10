package org.gecko.view.inspector.element.button;

import javafx.scene.control.Button;
import org.gecko.view.inspector.element.InspectorElement;

/**
 * An abstract representation of a type of {@link Button}, implementing the {@link InspectorElement} interface.
 */
public abstract class AbstractInspectorButton extends Button implements InspectorElement<Button> {
    private static final String ICON_STYLE_NAME = "inspector-button";
    protected static final String BUTTONS = "Buttons";
    private static final int DEFAULT_SIZE = 24;

    protected AbstractInspectorButton() {
        getStyleClass().add(ICON_STYLE_NAME);
        setPrefSize(DEFAULT_SIZE, DEFAULT_SIZE);
    }

    @Override
    public Button getControl() {
        return this;
    }
}
