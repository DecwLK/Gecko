package org.gecko.tools;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.viewelement.SystemViewElement;

public class PanTool extends Tool {

    public PanTool(ActionManager actionManager) {
        super(actionManager, ToolType.PAN);
    }

    @Override
    public void visitView(VBox vbox, ScrollPane view, Group worldGroup, Group containerGroup) {
        super.visitView(vbox, view, worldGroup, containerGroup);
        view.setPannable(true);
        view.setCursor(Cursor.OPEN_HAND);
    }
}
