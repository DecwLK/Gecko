package org.gecko.tools;

import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;

public class SystemCreatorTool extends Tool {
    public SystemCreatorTool(ActionManager actionManager) {
        super(actionManager, ToolType.SYSTEM_CREATOR_TOOL);
    }

    @Override
    public void visitView(ScrollPane view) {
        super.visitView(view);
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
