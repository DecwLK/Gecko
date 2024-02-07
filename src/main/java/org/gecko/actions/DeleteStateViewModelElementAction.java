package org.gecko.actions;

import java.util.Set;
import org.gecko.exceptions.GeckoException;
import org.gecko.model.Automaton;
import org.gecko.model.State;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class DeleteStateViewModelElementAction extends AbstractPositionableViewModelElementAction {
    private final GeckoViewModel geckoViewModel;
    private final StateViewModel stateViewModel;
    private final Automaton automaton;
    private final SystemViewModel systemViewModel;
    private boolean wasStartState;

    DeleteStateViewModelElementAction(
        GeckoViewModel geckoViewModel, StateViewModel stateViewModel, SystemViewModel systemViewModel) {
        this.geckoViewModel = geckoViewModel;
        this.stateViewModel = stateViewModel;
        this.automaton = systemViewModel.getTarget().getAutomaton();
        this.systemViewModel = systemViewModel;
        this.wasStartState = automaton.getStartState() == stateViewModel.getTarget();
    }

    @Override
    boolean run() throws GeckoException {
        if (wasStartState && automaton.getStates().size() > 1) {
            Set<State> states = automaton.getStates();
            State newStartState = states.stream().filter(s -> s != stateViewModel.getTarget()).findFirst().get();
            StateViewModel newStartStateViewModel = (StateViewModel) geckoViewModel.getViewModelElement(newStartState);
            systemViewModel.setStartState(newStartStateViewModel);
            systemViewModel.updateTarget();
        }
        automaton.removeState(stateViewModel.getTarget());
        geckoViewModel.deleteViewModelElement(stateViewModel);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return new RestoreStateViewModelElementAction(geckoViewModel, stateViewModel, systemViewModel, wasStartState);
    }

    @Override
    PositionableViewModelElement<?> getTarget() {
        return stateViewModel;
    }
}
