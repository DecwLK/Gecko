package org.gecko.view.inspector.element.button;

import javafx.scene.control.Tooltip;
import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.views.shortcuts.Shortcuts;

/**
 * Represents a type of {@link AbstractInspectorButton} used for navigating back in the selection history of the
 * {@link org.gecko.viewmodel.SelectionManager SelectionManager}.
 */
public class InspectorSelectionBackwardButton extends AbstractInspectorButton {
    private static final String ICON_STYLE_NAME = "inspector-selection-backward-button";

    public InspectorSelectionBackwardButton(ActionManager actionManager) {
        getStyleClass().add(ICON_STYLE_NAME);
        String toolTip = "%s (%s)".formatted(ResourceHandler.getString("Tooltips", "inspector_selection_backward"),
            Shortcuts.SELECTION_BACK.get().getDisplayText());
        setTooltip(new Tooltip(toolTip));
        setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory().createSelectionHistoryBackAction());
        });
    }
}
