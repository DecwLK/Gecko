package org.gecko.tools;

import javafx.scene.input.KeyCode;
import lombok.Getter;

@Getter
public enum ToolType {
    CURSOR_TOOL("Cursor Tool", "cursor-icon", KeyCode.A), MARQUEE_TOOL("Marquee Tool", "marquee-icon", KeyCode.M),
    PAN_TOOL("Pan Tool", "pan-icon", KeyCode.H), ZOOM_TOOL("Zoom Tool", "zoom-icon", KeyCode.Z),
    SYSTEM_CREATOR_TOOL("System Creator Tool", "system-creator-icon", KeyCode.Y),
    STATE_CREATOR_TOOL("State Creator Tool", "state-creator-icon", KeyCode.S),
    EDGE_CREATOR_TOOL("Edge Creator Tool", "edge-icon", KeyCode.E),
    REGION_CREATOR_TOOL("Region Creator Tool", "region-creator-icon", KeyCode.R),
    VARIABLE_BLOCK_CREATOR_TOOL("Variable Block Creator Tool", "variable-block-creator-icon", KeyCode.V),
    CONNECTION_CREATOR_TOOL("Connection Creator Tool", "edge-icon", KeyCode.C);

    private final String label;
    private final String icon;
    private final KeyCode keyCode;

    ToolType(String label, String icon, KeyCode keyCode) {
        this.label = label;
        this.icon = icon;
        this.keyCode = keyCode;
    }
}
