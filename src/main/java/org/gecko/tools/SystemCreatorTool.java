package org.gecko.tools;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;

public class SystemCreatorTool extends Tool {
    public SystemCreatorTool(ActionManager actionManager) {
        super(actionManager, ToolType.SYSTEM_CREATOR);
    }

    @Override
    public void visitView(VBox vbox, ScrollPane view, Group worldGroup, Group containerGroup) {
        super.visitView(vbox, view, worldGroup, containerGroup);
        view.setOnMouseClicked(event -> {
            if (event.isConsumed()) {
                return;
            }

            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }
            Action createSystemAction = actionManager.getActionFactory()
                .createCreateSystemViewModelElementAction(new Point2D(event.getX(), event.getY()));
            actionManager.run(createSystemAction);
        });
    }
}
