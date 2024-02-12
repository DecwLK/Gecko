package org.gecko.view.inspector.element.textfield;

import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.RegionViewModel;

/**
 * A concrete representation of an {@link InspectorAreaField} for a {@link RegionViewModel}, through which the invariant
 * of the region can be changed.
 */
public class InspectorInvariantField extends InspectorAreaField {
    private final ActionManager actionManager;
    private final RegionViewModel regionViewModel;

    public InspectorInvariantField(ActionManager actionManager, RegionViewModel regionViewModel) {
        super(actionManager, regionViewModel.getInvariantProperty(), false);
        this.actionManager = actionManager;
        this.regionViewModel = regionViewModel;
    }

    @Override
    protected Action getAction() {
        return actionManager.getActionFactory().createChangeInvariantViewModelElementAction(regionViewModel, getText());
    }
}
