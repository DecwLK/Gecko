package org.gecko.tools;

import javafx.scene.input.KeyCodeCombination;
import lombok.Getter;
import org.gecko.view.views.shortcuts.Shortcuts;

@Getter
public enum ToolType {
    CURSOR("Cursor Tool", "cursor-icon", Shortcuts.CURSOR_TOOL.get()),
    MARQUEE_TOOL("Marquee Tool", "marquee-icon", Shortcuts.MARQUEE_TOOL.get()),
    PAN("Pan Tool", "pan-icon", Shortcuts.PAN_TOOL.get()),
    ZOOM_TOOL("Zoom Tool", "zoom-icon", Shortcuts.ZOOM_TOOL.get()),
    SYSTEM_CREATOR("System Creator Tool", "system-creator-icon", Shortcuts.SYSTEM_CREATOR.get()),
    STATE_CREATOR("State Creator Tool", "state-creator-icon", Shortcuts.STATE_CREATOR.get()),
    EDGE_CREATOR("Edge Creator Tool", "edge-icon", Shortcuts.EDGE_CREATOR.get()),
    REGION_CREATOR("Region Creator Tool", "region-creator-icon", Shortcuts.REGION_CREATOR.get()),
    VARIABLE_BLOCK_CREATOR("Variable Block Creator Tool", "variable-block-creator-icon",
        Shortcuts.VARIABLE_BLOCK_CREATOR.get()),
    CONNECTION_CREATOR("Connection Creator Tool", "edge-icon", Shortcuts.CONNECTION_CREATOR.get());

    private final String label;
    private final String icon;
    private final KeyCodeCombination keyCodeCombination;

    ToolType(String label, String icon, KeyCodeCombination keyCodeCombination) {
        this.label = label;
        this.icon = icon;
        this.keyCodeCombination = keyCodeCombination;
    }
}
