package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.SystemViewModel;

public class InspectorOpenSystemButton extends AbstractInspectorButton {
    public InspectorOpenSystemButton(ActionManager actionManager, SystemViewModel systemViewModel) {
        setOnAction(event -> actionManager.run(actionManager.getActionFactory().createViewSwitchAction(systemViewModel, false)));
    }
}
