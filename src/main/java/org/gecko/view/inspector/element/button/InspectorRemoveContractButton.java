package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.StateViewModel;

public class InspectorRemoveContractButton extends AbstractInspectorButton {
    public InspectorRemoveContractButton(
            ActionManager actionManager, StateViewModel stateViewModel, ContractViewModel contractViewModel) {
        setOnAction(
                event -> {
                    actionManager.run(
                            actionManager
                                    .getActionFactory()
                                    .createDeleteContractViewModelAction(stateViewModel, contractViewModel));
                });
    }
}
