package org.gecko.tools;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.ViewElementPane;

/**
 * A concrete representation of a state-creating-{@link Tool}, utilized for creating a
 * {@link org.gecko.viewmodel.StateViewModel StateViewModel}.
 */
public class StateCreatorTool extends Tool {

    public StateCreatorTool(ActionManager actionManager) {
        super(actionManager, ToolType.STATE_CREATOR, false);
    }

    @Override
    public void visitView(ViewElementPane pane) {
        super.visitView(pane);
        pane.draw().setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }

            Point2D position = pane.screenToWorldCoordinates(event.getScreenX(), event.getScreenY());
            Action createStateAction =
                actionManager.getActionFactory().createCreateStateViewModelElementAction(position);
            actionManager.run(createStateAction);
        });
    }
}
