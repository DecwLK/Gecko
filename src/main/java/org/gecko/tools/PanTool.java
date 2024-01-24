package org.gecko.tools;

import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import org.gecko.actions.ActionManager;

public class PanTool extends Tool {

    private static final String NAME = "Pan Tool";
    private static final String ICON_STYLE_NAME = "pan-icon";

    public PanTool(ActionManager actionManager) {
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
        view.setPannable(true);
        view.setCursor(Cursor.OPEN_HAND);
    }
}
