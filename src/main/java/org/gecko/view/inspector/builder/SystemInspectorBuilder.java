package org.gecko.view.inspector.builder;

import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorSeparator;
import org.gecko.view.inspector.element.button.InspectorAddVariableButton;
import org.gecko.view.inspector.element.button.InspectorOpenSystemButton;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.view.inspector.element.list.InspectorVariableList;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class SystemInspectorBuilder extends AbstractInspectorBuilder<SystemViewModel> {

    public SystemInspectorBuilder(
            ActionManager actionManager,
            EditorViewModel editorViewModel,
            SystemViewModel viewModel) {
        super(actionManager, viewModel);

        // Open system button
        addInspectorElement(
                new InspectorOpenSystemButton(actionManager, viewModel));

        addInspectorElement(new InspectorSeparator());

        // Variables
        addInspectorElement(new InspectorLabel("L: Input"));
        addInspectorElement(
                new InspectorAddVariableButton(actionManager, viewModel));
        // TODO
        addInspectorElement(new InspectorVariableList());

        addInspectorElement(new InspectorLabel("L: Output"));
        addInspectorElement(
                new InspectorAddVariableButton(actionManager, viewModel));
        // TODO
        addInspectorElement(new InspectorVariableList());
    }
}
