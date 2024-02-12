package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.model.Automaton;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

/**
 * A concrete representation of an {@link Action} that removes the target of an {@link EdgeViewModel} from the given
 * {@link Automaton} and the afferent {@link EdgeViewModel} from the list of outgoing- and
 * ingoing-{@link EdgeViewModel}s of its source- and destination-{@link org.gecko.viewmodel.StateViewModel}s.
 */
public class DeleteEdgeViewModelElementAction extends AbstractPositionableViewModelElementAction {
    private final GeckoViewModel geckoViewModel;
    private final EdgeViewModel edgeViewModel;
    private final Automaton automaton;

    DeleteEdgeViewModelElementAction(GeckoViewModel geckoViewModel, EdgeViewModel edgeViewModel, Automaton automaton) {
        this.geckoViewModel = geckoViewModel;
        this.edgeViewModel = edgeViewModel;
        this.automaton = automaton;
    }

    @Override
    boolean run() throws GeckoException {
        edgeViewModel.getSource().getOutgoingEdges().remove(edgeViewModel);
        edgeViewModel.getDestination().getIncomingEdges().remove(edgeViewModel);
        automaton.removeEdge(edgeViewModel.getTarget());
        geckoViewModel.deleteViewModelElement(edgeViewModel);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return new RestoreEdgeViewModelElementAction(geckoViewModel, edgeViewModel, automaton);
    }

    @Override
    PositionableViewModelElement<?> getTarget() {
        return edgeViewModel;
    }
}
