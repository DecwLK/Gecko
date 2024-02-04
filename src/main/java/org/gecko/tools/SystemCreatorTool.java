package org.gecko.tools;

import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;

public class SystemCreatorTool extends Tool {
    public SystemCreatorTool(ActionManager actionManager) {
        super(actionManager, ToolType.SYSTEM_CREATOR);
    }

    @Override
    public void visitView(VBox vbox, ScrollPane view) {
        super.visitView(vbox, view);
        view.setOnMouseClicked(event -> {
            if (event.isConsumed()) {
                return;
            }
            Action createSystemAction = actionManager.getActionFactory()
                .createCreateSystemViewModelElementAction(new Point2D(event.getX(), event.getY()));
            actionManager.run(createSystemAction);
        });
    }
}
