package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.SelectionManager;

public class InspectorSelectionForwardButton extends AbstractInspectorButton {
    public InspectorSelectionForwardButton(
            ActionManager actionManager, SelectionManager selectionManager) {

        setOnAction(
                event -> {
                    actionManager.run(
                            actionManager
                                    .getActionFactory()
                                    .createSelectionHistoryForwardAction(selectionManager));
                });
    }
}
