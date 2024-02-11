package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;

/** A concrete representation of an {@link Action} that sets a {@link StateViewModel} as start state in the current {@link SystemViewModel}. Additionally, holds the previous start-{@link StateViewModel}. */
public class SetStartStateViewModelElementAction extends Action {
    private final GeckoViewModel geckoViewModel;
    private final StateViewModel stateViewModel;
    private StateViewModel previousStartState;

    SetStartStateViewModelElementAction(GeckoViewModel geckoViewModel, StateViewModel stateViewModel) {
        this.geckoViewModel = geckoViewModel;
        this.stateViewModel = stateViewModel;
    }

    @Override
    boolean run() throws GeckoException {
        SystemViewModel systemViewModel = geckoViewModel.getCurrentEditor().getCurrentSystem();
        previousStartState = systemViewModel.getStartState();
        systemViewModel.setStartState(stateViewModel);
        systemViewModel.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createSetStartStateViewModelElementAction(previousStartState);
    }
}
