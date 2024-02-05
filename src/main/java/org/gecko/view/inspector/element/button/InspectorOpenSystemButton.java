package org.gecko.view.inspector.element.button;

import javafx.scene.control.Tooltip;
import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.views.shortcuts.Shortcuts;
import org.gecko.viewmodel.SystemViewModel;

public class InspectorOpenSystemButton extends AbstractInspectorButton {

    private static final int WIDTH = 300;

    public InspectorOpenSystemButton(ActionManager actionManager, SystemViewModel systemViewModel) {
        setText(ResourceHandler.getString("Buttons", "inspector_open_system"));
        setPrefWidth(WIDTH);
        setOnAction(event -> actionManager.run(
            actionManager.getActionFactory().createViewSwitchAction(systemViewModel, false)));
        setTooltip(new Tooltip(Shortcuts.OPEN_CHILD_SYSTEM_EDITOR.get().getDisplayText()));
    }
}
