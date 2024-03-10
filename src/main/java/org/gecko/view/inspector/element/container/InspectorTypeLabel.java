package org.gecko.view.inspector.element.container;

import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.inspector.builder.AbstractInspectorBuilder;
import org.gecko.view.inspector.element.combobox.InspectorTypeComboBox;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.PortViewModel;

/**
 * Represents a type of {@link LabeledInspectorElement}. Contains an {@link InspectorLabel} and an
 * {@link InspectorTypeComboBox}.
 */
public class InspectorTypeLabel extends LabeledInspectorElement {
    protected static final String TYPE_KEY = "type";

    public InspectorTypeLabel(ActionManager actionManager, PortViewModel viewModel) {
        super(new InspectorLabel(ResourceHandler.getString(AbstractInspectorBuilder.INSPECTOR, TYPE_KEY)),
            new InspectorTypeComboBox(actionManager, viewModel));
    }
}
