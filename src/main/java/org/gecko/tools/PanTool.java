package org.gecko.tools;

import javafx.scene.Cursor;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.ViewElementPane;

/**
 * A concrete representation of a pan-{@link Tool}, utilized for moving the view.
 */
public class PanTool extends Tool {

    public PanTool(ActionManager actionManager) {
        super(actionManager, ToolType.PAN, true);
    }

    @Override
    public void visitView(ViewElementPane pane) {
        super.visitView(pane);
        pane.draw().setPannable(true);
        pane.draw().setCursor(Cursor.OPEN_HAND);
    }
}
