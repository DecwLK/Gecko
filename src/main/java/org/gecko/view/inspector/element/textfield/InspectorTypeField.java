package org.gecko.view.inspector.element.textfield;

import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.PortViewModel;

/** A concrete representation of an {@link InspectorTextField} for a {@link PortViewModel}, through which the type of the port can be changed. */
public class InspectorTypeField extends InspectorTextField {
    private final PortViewModel viewModel;
    private final ActionManager actionManager;

    public InspectorTypeField(ActionManager actionManager, PortViewModel viewModel) {
        super(viewModel.getTypeProperty(), actionManager);
        this.viewModel = viewModel;
        this.actionManager = actionManager;
    }

    @Override
    protected Action getAction() {
        return actionManager.getActionFactory().createChangeTypePortViewModelElementAction(viewModel, getText());
    }
}
