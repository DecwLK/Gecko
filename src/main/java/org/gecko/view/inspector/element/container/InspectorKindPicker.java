package org.gecko.view.inspector.element.container;

import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.combobox.InspectorKindComboBox;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.EdgeViewModel;

public class InspectorKindPicker extends LabeledInspectorElement {

    public InspectorKindPicker(ActionManager actionManager, EdgeViewModel viewModel) {
        super(new InspectorLabel("L:Kind"), new InspectorKindComboBox(actionManager, viewModel));
    }
}
