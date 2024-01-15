package org.gecko.actions;

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
    void run() {
        edgeViewModel.setPriority(newPriority);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createModifyEdgeViewModelPriorityAction(edgeViewModel, oldPriority);
    }
}
