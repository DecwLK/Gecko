package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.EdgeViewModel;

public class ModifyEdgeViewModelPriorityAction extends Action {

    private final EdgeViewModel edgeViewModel;
    private final int newPriority;

    private final int oldPriority;

    ModifyEdgeViewModelPriorityAction(EdgeViewModel edgeViewModel, int newPriority) {
        this.edgeViewModel = edgeViewModel;
        this.newPriority = newPriority;
        this.oldPriority = edgeViewModel.getPriority();
    }

    @Override
    boolean run() throws GeckoException {
        edgeViewModel.setPriority(newPriority);
        edgeViewModel.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createModifyEdgeViewModelPriorityAction(edgeViewModel, oldPriority);
    }
}
