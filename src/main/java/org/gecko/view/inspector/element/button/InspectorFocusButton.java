package org.gecko.view.inspector.element.button;

import javafx.scene.control.Tooltip;
import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.viewmodel.PositionableViewModelElement;

/**
 * Represents a type of {@link AbstractInspectorButton} used for focusing on a {@link PositionableViewModelElement}.
 */
public class InspectorFocusButton extends AbstractInspectorButton {

    private static final String ICON_STYLE_NAME = "inspector-focus-element-button";

    public InspectorFocusButton(ActionManager actionManager, PositionableViewModelElement<?> element) {
        getStyleClass().add(ICON_STYLE_NAME);
        setTooltip(new Tooltip(ResourceHandler.getString("Tooltips", "inspector_focus_element")));
        setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory().createFocusPositionableViewModelElementAction(element));
        });
    }
}
