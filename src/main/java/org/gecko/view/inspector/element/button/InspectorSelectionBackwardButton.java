package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;

public class InspectorSelectionBackwardButton extends AbstractInspectorButton {
    public InspectorSelectionBackwardButton(ActionManager actionManager) {

        setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory().createSelectionHistoryBackAction());
        });
    }
}
