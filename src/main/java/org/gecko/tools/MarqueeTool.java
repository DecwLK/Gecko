package org.gecko.tools;

import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import org.gecko.actions.ActionManager;

public class MarqueeTool extends Tool {

    public MarqueeTool(ActionManager actionManager) {
        super(actionManager, ToolType.MARQUEE_TOOL);
    }

    @Override
    public void visitView(ScrollPane view) {
        super.visitView(view);
        view.setCursor(Cursor.CROSSHAIR);
    }
}
