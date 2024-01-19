package org.gecko.actions;

import org.gecko.viewmodel.RegionViewModel;

public class ChangeInvariantViewModelElementAction extends Action {

    private final RegionViewModel regionViewModel;
    private final String newInvariant;

    private final String oldInvariant;

    ChangeInvariantViewModelElementAction(RegionViewModel regionViewModel, String newInvariant) {
        this.regionViewModel = regionViewModel;
        this.newInvariant = newInvariant;
        this.oldInvariant = regionViewModel.getInvariant();
    }

    @Override
    void run() {
        regionViewModel.setInvariant(newInvariant);
        regionViewModel.updateTarget();
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangeInvariantViewModelElementAction(regionViewModel, oldInvariant);
    }
}
