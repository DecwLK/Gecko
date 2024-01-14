package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class InspectorOpenSystemButton extends AbstractInspectorButton {
    public InspectorOpenSystemButton(
            ActionManager actionManager,
            EditorViewModel editorViewModel,
            SystemViewModel systemViewModel) {
        setOnAction(
                event ->
                        actionManager.run(
                                actionManager
                                        .getActionFactory()
                                        .createViewSwitchAction(
                                                systemViewModel, editorViewModel, false)));
    }
}
