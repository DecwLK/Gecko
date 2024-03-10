package org.gecko.view.inspector.element.container;

import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.inspector.builder.AbstractInspectorBuilder;
import org.gecko.view.inspector.element.combobox.InspectorKindComboBox;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.EdgeViewModel;

/**
 * Represents a type of {@link LabeledInspectorElement}. Contains an {@link InspectorLabel} and an
 * {@link InspectorKindComboBox}.
 */
public class InspectorKindPicker extends LabeledInspectorElement {
    private static final String KIND_KEY = "kind";

    public InspectorKindPicker(ActionManager actionManager, EdgeViewModel viewModel) {
        super(new InspectorLabel(ResourceHandler.getString(AbstractInspectorBuilder.INSPECTOR, KIND_KEY)),
            new InspectorKindComboBox(actionManager, viewModel));
    }
}
