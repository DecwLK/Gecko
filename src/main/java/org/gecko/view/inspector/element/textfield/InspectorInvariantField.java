package org.gecko.view.inspector.element.textfield;

import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.RegionViewModel;

public class InspectorInvariantField extends InspectorContractField {
    private final ActionManager actionManager;
    private final RegionViewModel regionViewModel;

    public InspectorInvariantField(ActionManager actionManager, RegionViewModel regionViewModel) {
        super(actionManager, regionViewModel.getInvariantProperty());
        this.actionManager = actionManager;
        this.regionViewModel = regionViewModel;
    }

    @Override
    protected Action getAction() {
        return actionManager.getActionFactory().createChangeInvariantViewModelElementAction(regionViewModel, getText());
    }
}
