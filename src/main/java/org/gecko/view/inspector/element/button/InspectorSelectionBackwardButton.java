package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.SelectionManager;

public class InspectorSelectionBackwardButton extends AbstractInspectorButton {
    public InspectorSelectionBackwardButton(
            ActionManager actionManager, SelectionManager selectionManager) {

        setOnAction(
                event -> {
                    actionManager.run(
                            actionManager
                                    .getActionFactory()
                                    .createSelectionHistoryBackAction(selectionManager));
                });
    }
}
