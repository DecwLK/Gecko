package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.StateViewModel;

public class InspectorAddContractButton extends AbstractInspectorButton {
    public InspectorAddContractButton(ActionManager actionManager, StateViewModel stateViewModel) {
        setOnAction(
                event -> {
                    actionManager.run(
                            actionManager
                                    .getActionFactory()
                                    .createCreateContractViewModelElementAction(stateViewModel));
                });
    }
}
