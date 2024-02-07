package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.model.Automaton;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

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
        //TODO: remove edges from source and destination states
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
