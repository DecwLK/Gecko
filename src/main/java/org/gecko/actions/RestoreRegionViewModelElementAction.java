package org.gecko.actions;

import org.gecko.model.Automaton;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.RegionViewModel;

/** A concrete representation of an {@link Action} that restores a deleted {@link RegionViewModel} in a given {@link Automaton}. */
public class RestoreRegionViewModelElementAction extends Action {
    private final GeckoViewModel geckoViewModel;
    private final RegionViewModel regionViewModel;
    private final Automaton automaton;

    RestoreRegionViewModelElementAction(
        GeckoViewModel geckoViewModel, RegionViewModel regionViewModel, Automaton automaton) {
        this.geckoViewModel = geckoViewModel;
        this.regionViewModel = regionViewModel;
        this.automaton = automaton;
    }


    @Override
    boolean run() {
        automaton.addRegion(regionViewModel.getTarget());
        geckoViewModel.addViewModelElement(regionViewModel);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return new DeleteRegionViewModelElementAction(geckoViewModel, regionViewModel, automaton);
    }
}
