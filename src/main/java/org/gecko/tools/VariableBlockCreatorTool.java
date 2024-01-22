package org.gecko.tools;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;

public class VariableBlockCreatorTool extends Tool {

    private static final String NAME = "Variable Block Creator Tool";
    private static final String ICON_STYLE_NAME = "variable-block-creator-icon";

    public VariableBlockCreatorTool(ActionManager actionManager) {
        super(actionManager);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getIconStyleName() {
        return ICON_STYLE_NAME;
    }

    @Override
    public void visitView(ScrollPane view) {
        super.visitView(view);
        view.setCursor(Cursor.CROSSHAIR);
        view.setOnMouseClicked(event -> {
            Point2D position = new Point2D(event.getX(), event.getY());
            Action createVariableBlockAction = actionManager.getActionFactory().createCreateVariableAction(position);
            actionManager.run(createVariableBlockAction);
        });
    }
}
