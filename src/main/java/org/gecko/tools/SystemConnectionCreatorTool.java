package org.gecko.tools;

import org.gecko.actions.ActionManager;

public class SystemConnectionCreatorTool extends Tool {

    private static final String NAME = "System Connection Creator Tool";

    public SystemConnectionCreatorTool(ActionManager actionManager) {
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
