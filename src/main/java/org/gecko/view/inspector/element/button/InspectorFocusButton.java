package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class InspectorFocusButton extends AbstractInspectorButton {
    public InspectorFocusButton(
            ActionManager actionManager,
            EditorViewModel editorViewModel,
            PositionableViewModelElement<?> element) {
        setOnAction(
                event -> {
                    actionManager.run(
                            actionManager
                                    .getActionFactory()
                                    .createFocusPositionableViewModelElementAction(
                                            editorViewModel, element));
                });
    }
}
