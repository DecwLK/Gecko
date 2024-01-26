package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.PortViewModel;

public class InspectorRemoveVariableButton extends AbstractInspectorButton {
    private static final String ICON_STYLE_NAME = "inspector-remove-button";

    public InspectorRemoveVariableButton(
        ActionManager actionManager, PortViewModel portViewModel) {
        getStyleClass().add(ICON_STYLE_NAME);
        setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory()
                .createDeletePositionableViewModelElementAction(portViewModel));
        });
    }
}
