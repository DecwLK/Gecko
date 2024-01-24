package org.gecko.view.inspector.builder;

import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorSeparator;
import org.gecko.view.inspector.element.container.InspectorTypeLabel;
import org.gecko.view.inspector.element.container.InspectorVisibilityPicker;
import org.gecko.viewmodel.PortViewModel;

public class VariableBlockInspectorBuilder extends AbstractInspectorBuilder<PortViewModel> {

    public VariableBlockInspectorBuilder(ActionManager actionManager, PortViewModel viewModel) {
        super(actionManager, viewModel);

        // Visibility
        addInspectorElement(new InspectorVisibilityPicker(actionManager, viewModel));
        addInspectorElement(new InspectorSeparator());

        // Type
        addInspectorElement(new InspectorTypeLabel(actionManager, viewModel));
    }
}
