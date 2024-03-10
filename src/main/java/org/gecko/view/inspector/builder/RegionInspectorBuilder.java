package org.gecko.view.inspector.builder;

import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.inspector.Inspector;
import org.gecko.view.inspector.element.InspectorSeparator;
import org.gecko.view.inspector.element.container.InspectorContractItem;
import org.gecko.view.inspector.element.container.InspectorRegionColorItem;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.RegionViewModel;

/**
 * Represents a type of {@link AbstractInspectorBuilder} of an {@link Inspector} for a {@link RegionViewModel}. Adds to
 * the list of {@link org.gecko.view.inspector.element.InspectorElement InspectorElement}s, which are added to a built
 * {@link org.gecko.view.inspector.Inspector Inspector}, the following: an {@link InspectorRegionColorItem} and an
 * {@link InspectorContractItem}.
 */
public class RegionInspectorBuilder extends AbstractInspectorBuilder<RegionViewModel> {

    public RegionInspectorBuilder(ActionManager actionManager, RegionViewModel viewModel) {
        super(actionManager, viewModel);

        // Color
        addInspectorElement(new InspectorRegionColorItem(actionManager, viewModel));
        addInspectorElement(new InspectorSeparator());

        // Contracts
        addInspectorElement(new InspectorLabel(ResourceHandler.getString("Inspector", "contract")));
        addInspectorElement(new InspectorContractItem(actionManager, viewModel));
    }
}
