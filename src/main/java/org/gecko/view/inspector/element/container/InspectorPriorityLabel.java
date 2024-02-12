package org.gecko.view.inspector.element.container;

import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.view.inspector.element.textfield.InspectorPriorityField;
import org.gecko.viewmodel.EdgeViewModel;

/**
 * Represents a type of {@link LabeledInspectorElement}. Contains an {@link InspectorLabel} and an
 * {@link InspectorPriorityField}.
 */
public class InspectorPriorityLabel extends LabeledInspectorElement {

    public InspectorPriorityLabel(ActionManager actionManager, EdgeViewModel viewModel) {
        super(new InspectorLabel(ResourceHandler.getString("Inspector", "priority")),
            new InspectorPriorityField(actionManager, viewModel));
    }
}
