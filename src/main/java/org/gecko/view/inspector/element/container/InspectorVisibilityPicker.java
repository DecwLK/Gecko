package org.gecko.view.inspector.element.container;

import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.combobox.InspectorVisibilityComboBox;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.PortViewModel;

public class InspectorVisibilityPicker extends LabeledInspectorElement {

    public InspectorVisibilityPicker(ActionManager actionManager, PortViewModel viewModel) {
        super(new InspectorLabel("L:Visibility"), new InspectorVisibilityComboBox(actionManager, viewModel));
    }
}
