package org.gecko.tools;

import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import org.gecko.actions.ActionManager;

public class EdgeCreatorTool extends Tool {

    private static final String NAME = "Edge Creator Tool";
    private static final String ICON_STYLE_NAME = "edge-icon";

    public EdgeCreatorTool(ActionManager actionManager) {
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
        view.setCursor(Cursor.CROSSHAIR);
    }
}
