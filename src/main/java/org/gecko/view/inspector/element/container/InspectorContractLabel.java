package org.gecko.view.inspector.element.container;

import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.button.InspectorAddContractButton;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.StateViewModel;

public class InspectorContractLabel extends LabeledInspectorElement {

    public InspectorContractLabel(ActionManager actionManager, StateViewModel viewModel) {
        super(new InspectorLabel("L:Contract"), new InspectorAddContractButton(actionManager, viewModel));
    }
}
