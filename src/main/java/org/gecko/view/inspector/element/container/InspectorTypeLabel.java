package org.gecko.view.inspector.element.container;

import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.inspector.element.combobox.InspectorTypeComboBox;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.PortViewModel;

/**
 * Represents a type of {@link LabeledInspectorElement}. Contains an {@link InspectorLabel} and an
 * {@link InspectorTypeComboBox}.
 */
public class InspectorTypeLabel extends LabeledInspectorElement {

    public InspectorTypeLabel(ActionManager actionManager, PortViewModel viewModel) {
        super(new InspectorLabel(ResourceHandler.getString("Inspector", "type")),
            new InspectorTypeComboBox(actionManager, viewModel));
    }
}
