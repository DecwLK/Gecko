package org.gecko.view.inspector.builder;

import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorSeparator;
import org.gecko.view.inspector.element.combobox.InspectorContractComboBox;
import org.gecko.view.inspector.element.container.InspectorContractItem;
import org.gecko.view.inspector.element.container.InspectorRegionColorItem;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.RegionViewModel;

public class RegionInspectorBuilder extends AbstractInspectorBuilder<RegionViewModel> {

    public RegionInspectorBuilder(ActionManager actionManager, RegionViewModel viewModel) {
        super(actionManager, viewModel);

        // Color
        addInspectorElement(new InspectorRegionColorItem(viewModel));

        addInspectorElement(new InspectorSeparator());

        // Contracts
        addInspectorElement(new InspectorLabel("L:Contracts"));
        addInspectorElement(
                new InspectorContractItem(
                        actionManager, viewModel.getContract(), viewModel.getInvariant()));
    }
}
