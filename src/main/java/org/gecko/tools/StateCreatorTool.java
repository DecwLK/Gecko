package org.gecko.tools;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;

public class StateCreatorTool extends Tool {

    public StateCreatorTool(ActionManager actionManager) {
        super(actionManager, ToolType.STATE_CREATOR);
    }

    @Override
    public void visitView(VBox vbox, ScrollPane view, Group worldGroup, Group containerGroup) {
        super.visitView(vbox, view, worldGroup, containerGroup);
        view.setOnMouseClicked(event -> {
            Point2D position = new Point2D(event.getX(), event.getY());
            Action createStateAction =
                actionManager.getActionFactory().createCreateStateViewModelElementAction(position);
            actionManager.run(createStateAction);
        });
    }
}
