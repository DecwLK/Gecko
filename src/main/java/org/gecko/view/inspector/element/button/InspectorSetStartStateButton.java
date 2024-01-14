package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.StateViewModel;

public class InspectorSetStartStateButton extends AbstractInspectorButton {
    public InspectorSetStartStateButton(
            ActionManager actionManager,
            EditorViewModel editorViewModel,
            StateViewModel stateViewModel) {
        setOnAction(
                event -> {
                    actionManager.run(
                            actionManager
                                    .getActionFactory()
                                    .createSetStartStateViewModelElementAction(
                                            editorViewModel, stateViewModel));
                });
    }
}
