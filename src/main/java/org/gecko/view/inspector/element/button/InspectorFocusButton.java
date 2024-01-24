package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.PositionableViewModelElement;

public class InspectorFocusButton extends AbstractInspectorButton {

    private static final String ICON_STYLE_NAME = "inspector-focus-element-button";

    public InspectorFocusButton(ActionManager actionManager, PositionableViewModelElement<?> element) {
        getStyleClass().add(ICON_STYLE_NAME);
        setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory().createFocusPositionableViewModelElementAction(element));
        });
    }
}
