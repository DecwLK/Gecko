package org.gecko.tools;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;

public class VariableBlockCreatorTool extends Tool {

    public VariableBlockCreatorTool(ActionManager actionManager) {
        super(actionManager, ToolType.VARIABLE_BLOCK_CREATOR);
    }

    @Override
    public void visitView(VBox vbox, ScrollPane view, Group worldGroup, Group containerGroup) {
        super.visitView(vbox, view, worldGroup, containerGroup);
        view.setCursor(Cursor.CROSSHAIR);
        view.setOnMouseClicked(event -> {
            Point2D position = new Point2D(event.getX(), event.getY());
            Action createVariableBlockAction = actionManager.getActionFactory().createCreateVariableAction(position);
            actionManager.run(createVariableBlockAction);
        });
    }
}
