package org.gecko.view.inspector.element.container;

import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.view.inspector.element.textfield.InspectorPriorityField;
import org.gecko.viewmodel.EdgeViewModel;

public class InspectorPriorityLabel extends LabeledInspectorElement {

    public InspectorPriorityLabel(ActionManager actionManager, EdgeViewModel viewModel) {
        super(new InspectorLabel("L:Priority"), new InspectorPriorityField(actionManager, viewModel));
    }
}
