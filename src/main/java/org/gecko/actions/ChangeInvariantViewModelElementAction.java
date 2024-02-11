package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.RegionViewModel;

/** A concrete representation of an {@link Action} that changes the invariant of a {@link RegionViewModel}, which it holds a reference to. Additionally holds the old and new {@link String invariants}s of the region for undo/redo purposes*/
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
        if (newInvariant.isEmpty()) {
            return false;
        }
        regionViewModel.setInvariant(newInvariant);
        regionViewModel.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangeInvariantViewModelElementAction(regionViewModel, oldInvariant);
    }
}
