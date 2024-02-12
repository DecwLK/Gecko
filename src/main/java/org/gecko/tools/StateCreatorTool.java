package org.gecko.tools;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;

/**
 * A concrete representation of a state-creating-{@link Tool}, utilized for creating a
 * {@link org.gecko.viewmodel.StateViewModel StateViewModel}.
 */
public class StateCreatorTool extends Tool {

    public StateCreatorTool(ActionManager actionManager) {
        super(actionManager, ToolType.STATE_CREATOR);
    }

    @Override
    public void visitView(VBox vbox, ScrollPane view, Group worldGroup, Group containerGroup) {
        super.visitView(vbox, view, worldGroup, containerGroup);
        view.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }

            Point2D position = new Point2D(event.getX(), event.getY());
            Action createStateAction =
                actionManager.getActionFactory().createCreateStateViewModelElementAction(position);
            actionManager.run(createStateAction);
        });
    }
}
