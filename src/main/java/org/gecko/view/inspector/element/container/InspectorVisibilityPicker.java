package org.gecko.view.inspector.element.container;

import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.inspector.builder.AbstractInspectorBuilder;
import org.gecko.view.inspector.element.combobox.InspectorVisibilityComboBox;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.PortViewModel;

/**
 * Represents a type of {@link LabeledInspectorElement}. Contains an {@link InspectorLabel} and an
 * {@link InspectorVisibilityComboBox}.
 */
public class InspectorVisibilityPicker extends LabeledInspectorElement {
    private static final String VISIBILITY_KEY = "visibility";

    public InspectorVisibilityPicker(ActionManager actionManager, PortViewModel viewModel) {
        super(new InspectorLabel(ResourceHandler.getString(AbstractInspectorBuilder.INSPECTOR, VISIBILITY_KEY)),
            new InspectorVisibilityComboBox(actionManager, viewModel));
    }
}
