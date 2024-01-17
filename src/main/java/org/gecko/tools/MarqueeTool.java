package org.gecko.tools;

import org.gecko.actions.ActionManager;

public class MarqueeTool extends Tool {

    private static final String NAME = "Marquee Tool";

    public MarqueeTool(ActionManager actionManager) {
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
