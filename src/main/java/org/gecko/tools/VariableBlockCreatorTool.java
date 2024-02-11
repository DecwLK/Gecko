package org.gecko.tools;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;

/** A concrete representation of a variable-block-creating-{@link Tool}, utilized for creating a {@link org.gecko.viewmodel.PortViewModel}. */
public class VariableBlockCreatorTool extends Tool {

    public VariableBlockCreatorTool(ActionManager actionManager) {
        super(actionManager, ToolType.VARIABLE_BLOCK_CREATOR);
    }

    @Override
    public void visitView(VBox vbox, ScrollPane view, Group worldGroup, Group containerGroup) {
        super.visitView(vbox, view, worldGroup, containerGroup);
        view.setCursor(Cursor.CROSSHAIR);
        view.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }

            Point2D position = new Point2D(event.getX(), event.getY());
            Action createVariableBlockAction = actionManager.getActionFactory().createCreateVariableAction(position);
            actionManager.run(createVariableBlockAction);
        });
    }
}
