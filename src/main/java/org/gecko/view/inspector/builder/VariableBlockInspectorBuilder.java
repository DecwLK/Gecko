package org.gecko.view.inspector.builder;

import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.combobox.InspectorVisibilityComboBox;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.PortViewModel;

public class VariableBlockInspectorBuilder extends AbstractInspectorBuilder<PortViewModel> {

    public VariableBlockInspectorBuilder(ActionManager actionManager, PortViewModel viewModel) {
        super(actionManager, viewModel);

        // Visibility
        addInspectorElement(new InspectorVisibilityComboBox(viewModel.getVisibility()));
    }
}
