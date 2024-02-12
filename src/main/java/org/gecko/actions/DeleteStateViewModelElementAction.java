package org.gecko.actions;

import java.util.Set;
import java.util.stream.Collectors;
import org.gecko.exceptions.GeckoException;
import org.gecko.model.Automaton;
import org.gecko.model.State;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;

/**
 * A concrete representation of an {@link Action} that removes a {@link StateViewModel} from the {@link GeckoViewModel}
 * and its target-{@link State} from the given {@link Automaton}.
 */
public class DeleteStateViewModelElementAction extends AbstractPositionableViewModelElementAction {
    private final GeckoViewModel geckoViewModel;
    private final EditorViewModel editorViewModel;
    private final StateViewModel stateViewModel;
    private final Automaton automaton;
    private final SystemViewModel systemViewModel;
    private boolean wasStartState;

    DeleteStateViewModelElementAction(
        GeckoViewModel geckoViewModel, StateViewModel stateViewModel, SystemViewModel systemViewModel) {
        this.geckoViewModel = geckoViewModel;
        this.editorViewModel = geckoViewModel.getCurrentEditor();
        this.stateViewModel = stateViewModel;
        this.automaton = systemViewModel.getTarget().getAutomaton();
        this.systemViewModel = systemViewModel;
        this.wasStartState = stateViewModel.getTarget().equals(automaton.getStartState());
    }

    @Override
    boolean run() throws GeckoException {
        if (wasStartState && automaton.getStates().size() > 1) {
            Set<State> states = automaton.getStates();
            State newStartState = states.stream().filter(s -> !s.equals(stateViewModel.getTarget())).findFirst().get();
            StateViewModel newStartStateViewModel = (StateViewModel) geckoViewModel.getViewModelElement(newStartState);
            systemViewModel.setStartState(newStartStateViewModel);
        }

        // remove from region if it is in one
        Set<RegionViewModel> regionViewModels = editorViewModel.getContainedPositionableViewModelElementsProperty()
            .stream()
            .filter(element -> automaton.getRegions().contains(element.getTarget()))
            .map(element -> (RegionViewModel) element)
            .collect(Collectors.toSet());

        regionViewModels.forEach(regionViewModel -> regionViewModel.removeState(stateViewModel));

        if (automaton.getStates().size() == 1 && wasStartState) {
            systemViewModel.setStartState(null);
        }
        systemViewModel.updateTarget();
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
