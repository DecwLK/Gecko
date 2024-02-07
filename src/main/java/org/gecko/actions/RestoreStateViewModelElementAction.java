package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.model.Automaton;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class RestoreStateViewModelElementAction extends Action {
    private final GeckoViewModel geckoViewModel;
    private final StateViewModel stateViewModel;
    private final Automaton automaton;
    private final SystemViewModel systemViewModel;
    private final boolean wasStartState;

    RestoreStateViewModelElementAction(
        GeckoViewModel geckoViewModel, StateViewModel stateViewModel, SystemViewModel systemViewModel,
        boolean wasStartState) {
        this.geckoViewModel = geckoViewModel;
        this.stateViewModel = stateViewModel;
        this.systemViewModel = systemViewModel;
        this.automaton = systemViewModel.getTarget().getAutomaton();
        this.wasStartState = wasStartState;
    }


    @Override
    boolean run() throws GeckoException {
        automaton.addState(stateViewModel.getTarget());
        geckoViewModel.addViewModelElement(stateViewModel);
        if (wasStartState) {
            systemViewModel.setStartState(stateViewModel);
            systemViewModel.updateTarget();
        }
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return new DeleteStateViewModelElementAction(geckoViewModel, stateViewModel, systemViewModel);
    }
}
