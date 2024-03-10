package org.gecko.view.inspector.element.textfield;

import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.PortViewModel;

/**
 * A concrete representation of an {@link InspectorTextField} for a {@link PortViewModel}, through which the value of
 * the variable can be changed.
 */
public class InspectorVariableValueField extends InspectorTextField {
    private final ActionManager actionManager;
    private final PortViewModel portViewModel;

    public InspectorVariableValueField(ActionManager actionManager, PortViewModel portViewModel) {
        super(portViewModel.getValueProperty(), actionManager);
        this.actionManager = actionManager;
        this.portViewModel = portViewModel;
    }

    @Override
    protected Action getAction() {
        return actionManager.getActionFactory().createChangeVariableValuePortViewModelAction(portViewModel, getText());
    }

    @Override
    protected void updateText() {
        getParent().requestFocus();
        if (getText() != null && getText().equals(portViewModel.getValue())) {
            return;
        }
        actionManager.run(getAction());
        setText(portViewModel.getValue());
    }
}
