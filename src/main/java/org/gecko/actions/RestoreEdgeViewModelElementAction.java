package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.model.Automaton;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.GeckoViewModel;

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
        //add to source and destination states
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return new DeleteEdgeViewModelElementAction(geckoViewModel, edgeViewModel, automaton);
    }
}
