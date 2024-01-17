package org.gecko.tools;

import org.gecko.actions.ActionManager;

public class EdgeCreatorTool extends Tool {

    private static final String NAME = "Edge Creator Tool";

    public EdgeCreatorTool(ActionManager actionManager) {
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
