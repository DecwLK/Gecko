package org.gecko.view.inspector.element.container;

import javafx.scene.layout.HBox;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.view.inspector.element.button.InspectorFocusButton;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.StateViewModel;

public class InspectorEdgeStateLabel extends HBox implements InspectorElement<HBox> {
    public InspectorEdgeStateLabel(
            ActionManager actionManager,
            EditorViewModel editorViewModel,
            StateViewModel stateViewModel,
            String name) {
        getChildren()
                .addAll(
                        new InspectorLabel(name),
                        new InspectorLabel(stateViewModel.getName()),
                        new InspectorFocusButton(actionManager, editorViewModel, stateViewModel));
    }

    @Override
    public HBox getControl() {
        return this;
    }
}
