package org.gecko.view.inspector.builder;

import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.inspector.element.InspectorSeparator;
import org.gecko.view.inspector.element.combobox.InspectorContractComboBox;
import org.gecko.view.inspector.element.container.InspectorEdgeStateLabel;
import org.gecko.view.inspector.element.container.InspectorKindPicker;
import org.gecko.view.inspector.element.container.InspectorPriorityLabel;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.EdgeViewModel;

public class EdgeInspectorBuilder extends AbstractInspectorBuilder<EdgeViewModel> {

    public EdgeInspectorBuilder(ActionManager actionManager, EdgeViewModel viewModel) {
        super(actionManager, viewModel);

        // Kind
        addInspectorElement(new InspectorKindPicker(actionManager, viewModel));
        addInspectorElement(new InspectorSeparator());

        // Connected states
        addInspectorElement(new InspectorEdgeStateLabel(actionManager, viewModel.getSource(),
            ResourceHandler.getString("Inspector", "source")));
        addInspectorElement(new InspectorEdgeStateLabel(actionManager, viewModel.getSource(),
            ResourceHandler.getString("Inspector", "target")));
        addInspectorElement(new InspectorSeparator());

        // Priority
        addInspectorElement(new InspectorPriorityLabel(actionManager, viewModel));
        addInspectorElement(new InspectorSeparator());

        // Contracts
        addInspectorElement(new InspectorLabel(ResourceHandler.getString("Inspector", "contract_plural")));
        addInspectorElement(new InspectorContractComboBox(actionManager, viewModel));
    }
}
