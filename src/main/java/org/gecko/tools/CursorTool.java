package org.gecko.tools;

import javafx.scene.control.ScrollPane;
import org.gecko.actions.ActionManager;

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
        view.setCursor(javafx.scene.Cursor.DEFAULT);
        view.setPannable(false);
    }
}
