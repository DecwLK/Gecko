package org.gecko.tools;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.ViewElementPane;

/**
 * A concrete representation of a variable-block-creating-{@link Tool}, utilized for creating a
 * {@link org.gecko.viewmodel.PortViewModel PortViewModel}.
 */
public class VariableBlockCreatorTool extends Tool {

    public VariableBlockCreatorTool(ActionManager actionManager) {
        super(actionManager, ToolType.VARIABLE_BLOCK_CREATOR);
    }

    @Override
    public void visitView(ViewElementPane pane) {
        super.visitView(pane);
        pane.draw().setCursor(Cursor.CROSSHAIR);
        pane.draw().setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }

            Point2D position = pane.screenToWorldCoordinates(event.getScreenX(), event.getScreenY());
            Action createVariableBlockAction = actionManager.getActionFactory().createCreateVariableAction(position);
            actionManager.run(createVariableBlockAction);
        });
    }
}
