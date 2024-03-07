package org.gecko.tools;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.ViewElementPane;

/**
 * A concrete representation of a system-creating-{@link Tool}, utilized for creating a
 * {@link org.gecko.viewmodel.SystemViewModel SystemViewModel}.
 */
public class SystemCreatorTool extends Tool {
    public SystemCreatorTool(ActionManager actionManager) {
        super(actionManager, ToolType.SYSTEM_CREATOR);
    }

    @Override
    public void visitView(ViewElementPane pane) {
        super.visitView(pane);
        pane.draw().setOnMouseClicked(event -> {
            if (event.isConsumed()) {
                return;
            }
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }
            Point2D point = pane.screenToWorldCoordinates(new Point2D(event.getScreenX(), event.getScreenY()));
            Action createSystemAction =
                actionManager.getActionFactory().createCreateSystemViewModelElementAction(point);
            actionManager.run(createSystemAction);
        });
    }
}
