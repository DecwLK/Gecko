package org.gecko.actions;

import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;

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
        previousStartState = (StateViewModel) geckoViewModel.getViewModelElement(
            geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget().getAutomaton().getStartState());
        geckoViewModel.getCurrentEditor()
            .getCurrentSystem()
            .getTarget()
            .getAutomaton()
            .setStartState(stateViewModel.getTarget());
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createSetStartStateViewModelElementAction(previousStartState);
    }
}
