package org.gecko.view.inspector.builder;

import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorSeparator;
import org.gecko.view.inspector.element.combobox.InspectorContractComboBox;
import org.gecko.view.inspector.element.combobox.InspectorKindComboBox;
import org.gecko.view.inspector.element.container.InspectorEdgeStateLabel;
import org.gecko.view.inspector.element.textfield.InspectorTextField;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.EditorViewModel;

public class EdgeInspectorBuilder extends AbstractInspectorBuilder<EdgeViewModel> {

    public EdgeInspectorBuilder(
            ActionManager actionManager, EditorViewModel editorViewModel, EdgeViewModel viewModel) {
        super(actionManager, viewModel);

        // Kind
        addInspectorElement(new InspectorKindComboBox(getViewModel().getKind()));
        addInspectorElement(new InspectorSeparator());

        // Connected states
        addInspectorElement(
                new InspectorEdgeStateLabel(
                        actionManager, editorViewModel, viewModel.getSource(), "L:Source"));

        addInspectorElement(
                new InspectorEdgeStateLabel(
                        actionManager, editorViewModel, viewModel.getSource(), "L:Target"));

        addInspectorElement(new InspectorSeparator());

        // Priority
        addInspectorElement(new InspectorTextField(viewModel.getPriority()));

        addInspectorElement(new InspectorSeparator());

        // Contracts
        addInspectorElement(new InspectorLabel("L:Contracts"));
        addInspectorElement(new InspectorContractComboBox(actionManager, viewModel));
    }
}
