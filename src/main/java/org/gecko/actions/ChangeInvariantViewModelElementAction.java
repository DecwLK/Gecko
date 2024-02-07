package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
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
    boolean run() throws GeckoException {
        regionViewModel.setInvariant(newInvariant);
        regionViewModel.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangeInvariantViewModelElementAction(regionViewModel, oldInvariant);
    }
}
