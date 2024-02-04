package org.gecko.tools;

import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.gecko.actions.ActionManager;

public class PanTool extends Tool {

    public PanTool(ActionManager actionManager) {
        super(actionManager, ToolType.PAN);
    }

    @Override
    public void visitView(VBox vbox, ScrollPane view) {
        super.visitView(vbox, view);
        view.setPannable(true);
        view.setCursor(Cursor.OPEN_HAND);
    }
}
