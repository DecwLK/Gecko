package org.gecko.view.inspector.element.textfield;

import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.PortViewModel;

/**
 * A concrete representation of an {@link InspectorAreaField} for a {@link PortViewModel}, through which the value of
 * the variable can be changed.
 */
public class InspectorVariableValueField extends InspectorAreaField {
    private final ActionManager actionManager;
    private final PortViewModel portViewModel;

    public InspectorVariableValueField(ActionManager actionManager, PortViewModel portViewModel) {
        super(actionManager, portViewModel.getValueProperty(), false);
        this.actionManager = actionManager;
        this.portViewModel = portViewModel;
    }

    @Override
    protected Action getAction() {
        return actionManager.getActionFactory().createChangeVariableValuePortViewModelAction(portViewModel, getText());
    }

}
