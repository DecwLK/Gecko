package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;

public class InspectorSelectionBackwardButton extends AbstractInspectorButton {
    private static final String ICON_STYLE_NAME = "inspector-selection-backward-button";

    public InspectorSelectionBackwardButton(ActionManager actionManager) {
        getStyleClass().add(ICON_STYLE_NAME);
        setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory().createSelectionHistoryBackAction());
        });
    }
}
