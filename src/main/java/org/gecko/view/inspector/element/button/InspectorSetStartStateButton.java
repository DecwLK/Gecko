package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.viewmodel.StateViewModel;

public class InspectorSetStartStateButton extends AbstractInspectorButton {
    public InspectorSetStartStateButton(ActionManager actionManager, StateViewModel stateViewModel) {
        setMaxWidth(Double.MAX_VALUE);
        setText(ResourceHandler.getString("Buttons", "inspector_set_start_state"));

        setOnAction(event -> {
            actionManager.run(
                actionManager.getActionFactory().createSetStartStateViewModelElementAction(stateViewModel));
        });
    }
}
