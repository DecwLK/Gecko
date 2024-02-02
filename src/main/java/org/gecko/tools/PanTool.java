package org.gecko.tools;

import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import org.gecko.actions.ActionManager;

public class PanTool extends Tool {

    public PanTool(ActionManager actionManager) {
        super(actionManager, ToolType.PAN_TOOL);
    }

    @Override
    public void visitView(ScrollPane view) {
        super.visitView(view);
        view.setPannable(true);
        view.setCursor(Cursor.OPEN_HAND);
    }
}
