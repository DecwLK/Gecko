package org.gecko.view.inspector.builder;

import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorSeparator;
import org.gecko.view.inspector.element.container.InspectorContractItem;
import org.gecko.view.inspector.element.container.InspectorRegionColorItem;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.RegionViewModel;

public class RegionInspectorBuilder extends AbstractInspectorBuilder<RegionViewModel> {

    public RegionInspectorBuilder(ActionManager actionManager, RegionViewModel viewModel) {
        super(actionManager, viewModel);

        // Color
        addInspectorElement(new InspectorRegionColorItem(actionManager, viewModel));
        addInspectorElement(new InspectorSeparator());

        // Contracts
        addInspectorElement(new InspectorLabel("L:Contract"));
        addInspectorElement(new InspectorContractItem(actionManager, viewModel));
    }
}
