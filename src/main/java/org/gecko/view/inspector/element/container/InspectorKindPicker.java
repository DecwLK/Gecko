package org.gecko.view.inspector.element.container;

import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.inspector.element.combobox.InspectorKindComboBox;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.EdgeViewModel;

/** Represents a type of {@link LabeledInspectorElement}. Contains an {@link InspectorLabel} and an {@link InspectorKindComboBox}. */
public class InspectorKindPicker extends LabeledInspectorElement {

    public InspectorKindPicker(ActionManager actionManager, EdgeViewModel viewModel) {
        super(new InspectorLabel(ResourceHandler.getString("Inspector", "kind")),
            new InspectorKindComboBox(actionManager, viewModel));
    }
}
