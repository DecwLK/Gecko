package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.StateViewModel;

public class InspectorAddContractButton extends AbstractInspectorButton {
    private static final int WIDTH = 70;

    public InspectorAddContractButton(ActionManager actionManager, StateViewModel stateViewModel) {
        setText("L:Add");
        setPrefWidth(WIDTH);
        setOnAction(event -> {
            actionManager.run(
                actionManager.getActionFactory().createCreateContractViewModelElementAction(stateViewModel));
        });
    }
}
