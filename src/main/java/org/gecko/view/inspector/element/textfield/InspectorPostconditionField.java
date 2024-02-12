package org.gecko.view.inspector.element.textfield;

import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.ContractViewModel;

/**
 * A concrete representation of an {@link InspectorAreaField} for a {@link ContractViewModel}, through which the
 * postcondition of the contract can be changed.
 */
public class InspectorPostconditionField extends InspectorAreaField {
    private final ActionManager actionManager;
    private final ContractViewModel contractViewModel;

    public InspectorPostconditionField(ActionManager actionManager, ContractViewModel contractViewModel) {
        super(actionManager, contractViewModel.getPostConditionProperty(), false);
        this.actionManager = actionManager;
        this.contractViewModel = contractViewModel;
    }

    @Override
    protected Action getAction() {
        return actionManager.getActionFactory()
            .createChangePostconditionViewModelElementAction(contractViewModel, getText());
    }
}
