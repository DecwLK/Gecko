package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.StateViewModel;

public class InspectorSetStartStateButton extends AbstractInspectorButton {
    public InspectorSetStartStateButton(ActionManager actionManager, StateViewModel stateViewModel) {
        setMaxWidth(Double.MAX_VALUE);
        setText("L:Set as start state");

        setOnAction(event -> {
            actionManager.run(
                actionManager.getActionFactory().createSetStartStateViewModelElementAction(stateViewModel));
        });
    }
}
