package org.gecko.view.inspector.element.button;

import javafx.scene.control.Tooltip;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.shortcuts.Shortcuts;

public class InspectorSelectionBackwardButton extends AbstractInspectorButton {
    private static final String ICON_STYLE_NAME = "inspector-selection-backward-button";

    public InspectorSelectionBackwardButton(ActionManager actionManager) {
        getStyleClass().add(ICON_STYLE_NAME);
        setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory().createSelectionHistoryBackAction());
        });
        setTooltip(new Tooltip(Shortcuts.SELECTION_BACK.get().getDisplayText()));
    }
}
