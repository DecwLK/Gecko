package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.StateViewModel;

public class InspectorRemoveContractButton extends AbstractInspectorButton {
    private static final String ICON_STYLE_NAME = "inspector-remove-button";

    public InspectorRemoveContractButton(ActionManager actionManager, StateViewModel stateViewModel, ContractViewModel contractViewModel) {
        getStyleClass().add(ICON_STYLE_NAME);
        setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory().createDeleteContractViewModelAction(stateViewModel, contractViewModel));
        });
    }
}
