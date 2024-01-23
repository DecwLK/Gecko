package org.gecko.tools;

import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.viewelement.StateViewElement;

public class CursorTool extends Tool {

    private static final String NAME = "Cursor Tool";
    private static final String ICON_STYLE_NAME = "cursor-icon";

    public CursorTool(ActionManager actionManager) {
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
        view.setCursor(Cursor.DEFAULT);
        view.setPannable(false);
    }

    @Override
    public void visit(StateViewElement stateViewElement) {
        super.visit(stateViewElement);
        stateViewElement.setOnMouseClicked(event -> {
            Action selectAction = actionManager.getActionFactory().createSelectAction(stateViewElement.getTarget(), !event.isControlDown());
            actionManager.run(selectAction);
        });
    }
}
