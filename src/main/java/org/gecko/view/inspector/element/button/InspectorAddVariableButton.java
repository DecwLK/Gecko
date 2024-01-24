package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.SystemViewModel;

public class InspectorAddVariableButton extends AbstractInspectorButton {

    private static final int WIDTH = 70;

    public InspectorAddVariableButton(ActionManager actionManager, SystemViewModel systemViewModel) {
        setText("L:Add");
        setPrefWidth(WIDTH);
        setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory().createCreatePortViewModelElementAction(systemViewModel));
        });
    }
}
