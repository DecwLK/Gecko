package org.gecko.view.inspector.element.button;

import javafx.scene.control.Tooltip;
import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.viewmodel.PortViewModel;

/**
 * Represents a type of {@link AbstractInspectorButton} used for removing a {@link PortViewModel}.
 */
public class InspectorRemoveVariableButton extends AbstractInspectorButton {
    private static final String ICON_STYLE_NAME = "inspector-remove-button";

    public InspectorRemoveVariableButton(ActionManager actionManager, PortViewModel portViewModel) {
        getStyleClass().add(ICON_STYLE_NAME);
        setTooltip(new Tooltip(ResourceHandler.getString("Tooltips", "inspector_remove_variable")));
        setOnAction(event -> {
            actionManager.run(
                actionManager.getActionFactory().createDeletePositionableViewModelElementAction(portViewModel));
        });
    }
}
