package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.SystemViewModel;

public class ChangeCodeSystemViewModelAction extends Action {

    private final SystemViewModel systemViewModel;
    private final String newCode;
    private final String oldCode;

    ChangeCodeSystemViewModelAction(SystemViewModel systemViewModel, String newCode) {
        this.systemViewModel = systemViewModel;
        this.newCode = newCode;
        this.oldCode = systemViewModel.getCode();
    }

    @Override
    boolean run() throws GeckoException {
        if (newCode.isEmpty()) {
            return false;
        }
        systemViewModel.setCode(newCode);
        systemViewModel.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangeCodeSystemViewModelAction(systemViewModel, oldCode);
    }
}
