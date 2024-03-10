package org.gecko.actions;

import org.gecko.exceptions.ModelException;
import org.gecko.viewmodel.PortViewModel;

public class ChangeVariableValuePortViewModelAction extends Action {
    private final PortViewModel portViewModel;
    private String newValue;
    private final String oldValue;

    ChangeVariableValuePortViewModelAction(PortViewModel portViewModel, String newValue) {
        this.portViewModel = portViewModel;
        this.newValue = newValue;
        this.oldValue = portViewModel.getValue();
    }

    @Override
    boolean run() throws ModelException {
        if (newValue == null || newValue.isEmpty()) {
            newValue = null;
        }
        portViewModel.setValue(newValue);
        portViewModel.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangeVariableValuePortViewModelAction(portViewModel, oldValue);
    }
}
