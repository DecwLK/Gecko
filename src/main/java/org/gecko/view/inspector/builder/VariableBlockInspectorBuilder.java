package org.gecko.view.inspector.builder;

import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorSeparator;
import org.gecko.view.inspector.element.container.InspectorTypeLabel;
import org.gecko.view.inspector.element.container.InspectorVisibilityPicker;
import org.gecko.viewmodel.PortViewModel;

/** Represents a type of {@link AbstractInspectorBuilder} of an {@link org.gecko.view.inspector.Inspector Inspector} for a {@link PortViewModel}. Adds to the list of {@link org.gecko.view.inspector.element.InspectorElement InspectorElement}s, which are added to a built {@link org.gecko.view.inspector.Inspector Inspector}, the following: an {@link InspectorVisibilityPicker} and an {@link InspectorTypeLabel}. */
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
