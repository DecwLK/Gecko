package org.gecko.view.inspector.builder;

import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorSeparator;
import org.gecko.view.inspector.element.combobox.InspectorContractComboBox;
import org.gecko.view.inspector.element.combobox.InspectorKindComboBox;
import org.gecko.view.inspector.element.container.InspectorEdgeStateLabel;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.view.inspector.element.textfield.InspectorPriorityField;
import org.gecko.viewmodel.EdgeViewModel;

public class EdgeInspectorBuilder extends AbstractInspectorBuilder<EdgeViewModel> {

    public EdgeInspectorBuilder(ActionManager actionManager, EdgeViewModel viewModel) {
        super(actionManager, viewModel);

        // Kind
        addInspectorElement(new InspectorKindComboBox(getViewModel().getKindProperty()));
        addInspectorElement(new InspectorSeparator());

        // Connected states
        addInspectorElement(new InspectorEdgeStateLabel(actionManager, viewModel.getSource(), "L:Source"));
        addInspectorElement(new InspectorEdgeStateLabel(actionManager, viewModel.getSource(), "L:Target"));
        addInspectorElement(new InspectorSeparator());

        // Priority
        addInspectorElement(new InspectorPriorityField(actionManager, viewModel));
        addInspectorElement(new InspectorSeparator());

        // Contracts
        addInspectorElement(new InspectorLabel("L:Contracts"));
        addInspectorElement(new InspectorContractComboBox(actionManager, viewModel));
    }
}
