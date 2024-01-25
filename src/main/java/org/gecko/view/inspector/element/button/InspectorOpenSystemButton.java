package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.SystemViewModel;

public class InspectorOpenSystemButton extends AbstractInspectorButton {

    private static final int WIDTH = 300;

    public InspectorOpenSystemButton(ActionManager actionManager, SystemViewModel systemViewModel) {
        setText("L:Open");
        setPrefWidth(WIDTH);
        setOnAction(event -> actionManager.run(
            actionManager.getActionFactory().createViewSwitchAction(systemViewModel, false)));
    }
}
