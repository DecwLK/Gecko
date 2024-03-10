package org.gecko.view.inspector.element.container;

import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.inspector.builder.AbstractInspectorBuilder;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.view.inspector.element.textfield.InspectorPriorityField;
import org.gecko.viewmodel.EdgeViewModel;

/**
 * Represents a type of {@link LabeledInspectorElement}. Contains an {@link InspectorLabel} and an
 * {@link InspectorPriorityField}.
 */
public class InspectorPriorityLabel extends LabeledInspectorElement {
    private static final String PRIORITY_KEY = "priority";

    public InspectorPriorityLabel(ActionManager actionManager, EdgeViewModel viewModel) {
        super(new InspectorLabel(ResourceHandler.getString(AbstractInspectorBuilder.INSPECTOR, PRIORITY_KEY)),
            new InspectorPriorityField(actionManager, viewModel));
    }
}
