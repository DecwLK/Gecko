package org.gecko.view.inspector.element.button;

import javafx.scene.control.Tooltip;
import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.views.shortcuts.Shortcuts;
import org.gecko.viewmodel.SystemViewModel;

/**
 * Represents a type of {@link AbstractInspectorButton} used for opening a {@link SystemViewModel} by switching to the
 * system view corresponding to the given system.
 */
public class InspectorOpenSystemButton extends AbstractInspectorButton {
    private static final String STYLE = "inspector-open-system-button";
    private static final int WIDTH = 300;

    public InspectorOpenSystemButton(ActionManager actionManager, SystemViewModel systemViewModel) {
        getStyleClass().add(STYLE);
        setText(ResourceHandler.getString("Buttons", "inspector_open_system"));
        String toolTip = "%s (%s)".formatted(ResourceHandler.getString("Tooltips", "inspector_open_system"),
            Shortcuts.OPEN_CHILD_SYSTEM_EDITOR.get().getDisplayText());
        setTooltip(new Tooltip(toolTip));
        setPrefWidth(WIDTH);
        setOnAction(event -> actionManager.run(
            actionManager.getActionFactory().createViewSwitchAction(systemViewModel, false)));
    }
}
