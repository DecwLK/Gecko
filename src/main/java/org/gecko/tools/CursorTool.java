package org.gecko.tools;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import org.gecko.actions.ActionManager;

public class CursorTool extends Tool {

    private static final String NAME = "Cursor Tool";

    public CursorTool(ActionManager actionManager) {
        super(actionManager);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getIconPath() {
        //TODO stub
        return null;
    }

    @Override
    public void visitView(ScrollPane view) {
        super.visitView(view);
        view.setCursor(javafx.scene.Cursor.DEFAULT);
        view.setPannable(false);
    }
}
