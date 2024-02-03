package org.gecko.actions;

import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class SetStartStateViewModelElementAction extends Action {
    private final GeckoViewModel geckoViewModel;
    private final StateViewModel stateViewModel;
    private StateViewModel previousStartState;

    SetStartStateViewModelElementAction(GeckoViewModel geckoViewModel, StateViewModel stateViewModel) {
        this.geckoViewModel = geckoViewModel;
        this.stateViewModel = stateViewModel;
    }

    @Override
    void run() {
        SystemViewModel systemViewModel = geckoViewModel.getCurrentEditor().getCurrentSystem();
        previousStartState = systemViewModel.getStartState();
        systemViewModel.setStartState(stateViewModel);
        systemViewModel.updateTarget();
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createSetStartStateViewModelElementAction(previousStartState);
    }
}
