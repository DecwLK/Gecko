package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.model.Automaton;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.GeckoViewModel;

/**
 * A concrete representation of an {@link Action} that restores a deleted {@link EdgeViewModel} in a given
 * {@link Automaton}.
 */
public class RestoreEdgeViewModelElementAction extends Action {
    private final GeckoViewModel geckoViewModel;
    private final EdgeViewModel edgeViewModel;
    private final Automaton automaton;

    RestoreEdgeViewModelElementAction(GeckoViewModel geckoViewModel, EdgeViewModel edgeViewModel, Automaton automaton) {
        this.geckoViewModel = geckoViewModel;
        this.edgeViewModel = edgeViewModel;
        this.automaton = automaton;
    }


    @Override
    boolean run() throws GeckoException {
        automaton.addEdge(edgeViewModel.getTarget());
        geckoViewModel.addViewModelElement(edgeViewModel);
        edgeViewModel.getSource().getOutgoingEdges().add(edgeViewModel);
        edgeViewModel.getDestination().getIncomingEdges().add(edgeViewModel);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return new DeleteEdgeViewModelElementAction(geckoViewModel, edgeViewModel, automaton);
    }
}
