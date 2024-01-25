package org.gecko.tools;

import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;

public class SystemCreatorTool extends Tool {

    private static final String NAME = "System Creator Tool";
    private static final String ICON_STYLE_NAME = "system-creator-icon";

    public SystemCreatorTool(ActionManager actionManager) {
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
