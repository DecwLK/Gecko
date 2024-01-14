package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.model.Visibility;
import org.gecko.viewmodel.SystemViewModel;

public class InspectorAddVariableButton extends AbstractInspectorButton {
    public InspectorAddVariableButton(ActionManager actionManager, SystemViewModel systemViewModel, Visibility visibility) {
        setOnAction(
                event -> {
                    actionManager.run(
                            actionManager
                                    .getActionFactory()
                                    .createCreateVariableAction(systemViewModel, visibility));
                });
    }
}
