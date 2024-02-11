package org.gecko.view.inspector.builder;

import org.gecko.actions.ActionManager;
import org.gecko.model.Visibility;
import org.gecko.view.inspector.element.InspectorSeparator;
import org.gecko.view.inspector.element.button.InspectorOpenSystemButton;
import org.gecko.view.inspector.element.container.InspectorVariableLabel;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.view.inspector.element.list.InspectorVariableList;
import org.gecko.view.inspector.element.textfield.InspectorCodeSystemField;
import org.gecko.viewmodel.SystemViewModel;

/** Represents a type of {@link AbstractInspectorBuilder} of an {@link org.gecko.view.inspector.Inspector Inspector} for a {@link SystemViewModel}. Adds to the list of {@link org.gecko.view.inspector.element.InspectorElement InspectorElement}s, which are added to a built {@link org.gecko.view.inspector.Inspector Inspector}, the following: an {@link InspectorOpenSystemButton} and two {@link InspectorVariableList}s for input- and output-{@link org.gecko.viewmodel.PortViewModel PortViewModel}s of the {@link SystemViewModel}. */
public class SystemInspectorBuilder extends AbstractInspectorBuilder<SystemViewModel> {

    public SystemInspectorBuilder(ActionManager actionManager, SystemViewModel viewModel) {
        super(actionManager, viewModel);

        // Open system button
        addInspectorElement(new InspectorOpenSystemButton(actionManager, viewModel));

        addInspectorElement(new InspectorSeparator());

        // Variables
        addInspectorElement(new InspectorVariableLabel(actionManager, viewModel, Visibility.INPUT));
        addInspectorElement(new InspectorVariableList(actionManager, viewModel, Visibility.INPUT));

        addInspectorElement(new InspectorVariableLabel(actionManager, viewModel, Visibility.OUTPUT));
        addInspectorElement(new InspectorVariableList(actionManager, viewModel, Visibility.OUTPUT));

        addInspectorElement(new InspectorLabel("Code"));
        addInspectorElement(new InspectorCodeSystemField(actionManager, viewModel));
    }
}
