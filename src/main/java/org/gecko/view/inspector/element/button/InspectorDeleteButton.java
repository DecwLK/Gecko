package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.PositionableViewModelElement;

public class InspectorDeleteButton extends AbstractInspectorButton {
    public InspectorDeleteButton(
            ActionManager actionManager,
            PositionableViewModelElement<?> elementToRemove) {
        setOnAction(
                event -> {
                    actionManager.run(
                            actionManager
                                    .getActionFactory()
                                    .createDeletePositionableViewModelElementAction(elementToRemove));
                });
    }
}
