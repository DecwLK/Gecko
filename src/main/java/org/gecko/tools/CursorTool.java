package org.gecko.tools;

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
}
