package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.model.Kind;
import org.gecko.viewmodel.EdgeViewModel;

public class ChangeKindEdgeViewModelAction extends Action {

    private final Kind kind;
    private final EdgeViewModel edgeViewModel;
    private Kind oldKind;

    public ChangeKindEdgeViewModelAction(EdgeViewModel edgeViewModel, Kind kind) {
        this.edgeViewModel = edgeViewModel;
        this.kind = kind;
    }

    @Override
    boolean run() throws GeckoException {
        oldKind = edgeViewModel.getKind();
        edgeViewModel.setKind(kind);
        edgeViewModel.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangeKindAction(edgeViewModel, oldKind);
    }
}
