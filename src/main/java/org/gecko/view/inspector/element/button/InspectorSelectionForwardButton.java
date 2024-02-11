package org.gecko.view.inspector.element.button;

import javafx.scene.control.Tooltip;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.shortcuts.Shortcuts;

/** Represents a type of {@link AbstractInspectorButton} used for navigating forward in the selection history of the {@link org.gecko.viewmodel.SelectionManager SelectionManager}. */
public class InspectorSelectionForwardButton extends AbstractInspectorButton {
    private static final String ICON_STYLE_NAME = "inspector-selection-forward-button";

    public InspectorSelectionForwardButton(ActionManager actionManager) {
        getStyleClass().add(ICON_STYLE_NAME);
        setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory().createSelectionHistoryForwardAction());
        });
        setTooltip(new Tooltip(Shortcuts.SELECTION_FORWARD.get().getDisplayText()));
    }
}
