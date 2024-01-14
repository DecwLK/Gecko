package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;

public class InspectorSelectionForwardButton extends AbstractInspectorButton {
    public InspectorSelectionForwardButton(
            ActionManager actionManager) {

        setOnAction(
                event -> {
                    actionManager.run(
                            actionManager
                                    .getActionFactory()
                                    .createSelectionHistoryForwardAction());
                });
    }
}
