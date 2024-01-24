package org.gecko.view.inspector.element.container;

import org.gecko.actions.ActionManager;
import org.gecko.model.Visibility;
import org.gecko.view.inspector.element.button.InspectorAddVariableButton;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.SystemViewModel;

public class InspectorVariableLabel extends LabeledInspectorElement {

    public InspectorVariableLabel(ActionManager actionManager, SystemViewModel viewModel, Visibility visibility) {
        super(new InspectorLabel(switch (visibility) {
            case INPUT -> "L:Input";
            case OUTPUT -> "L:Output";
            default -> "";
        }), new InspectorAddVariableButton(actionManager, viewModel));
    }
}
