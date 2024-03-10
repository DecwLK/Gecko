package org.gecko.tools;

import javafx.scene.input.KeyCodeCombination;
import lombok.Getter;
import org.gecko.view.ResourceHandler;
import org.gecko.view.views.shortcuts.Shortcuts;

/**
 * Enumerates the types of tools used in the Gecko Graphic Editor. Each {@link ToolType} is described by a label, an
 * icon and a {@link KeyCodeCombination}.
 */
@Getter
public enum ToolType {
    CURSOR(ResourceHandler.getString("Tools", "cursor"), "cursor-icon", Shortcuts.CURSOR_TOOL.get()),
    MARQUEE_TOOL(ResourceHandler.getString("Tools", "marquee"), "marquee-icon", Shortcuts.MARQUEE_TOOL.get()),
    PAN(ResourceHandler.getString("Tools", "pan"), "pan-icon", Shortcuts.PAN_TOOL.get()),
    ZOOM_TOOL(ResourceHandler.getString("Tools", "zoom"), "zoom-icon", Shortcuts.ZOOM_TOOL.get()),
    SYSTEM_CREATOR(ResourceHandler.getString("Tools", "system_creator"), "system-creator-icon",
        Shortcuts.SYSTEM_CREATOR.get()),
    STATE_CREATOR(ResourceHandler.getString("Tools", "state_creator"), "state-creator-icon",
        Shortcuts.STATE_CREATOR.get()),
    EDGE_CREATOR(ResourceHandler.getString("Tools", "edge_creator"), "edge-icon", Shortcuts.EDGE_CREATOR.get()),
    REGION_CREATOR(ResourceHandler.getString("Tools", "region_creator"), "region-creator-icon",
        Shortcuts.REGION_CREATOR.get()),
    VARIABLE_BLOCK_CREATOR(ResourceHandler.getString("Tools", "variable_block_creator"), "variable-block-creator-icon",
        Shortcuts.VARIABLE_BLOCK_CREATOR.get()),
    CONNECTION_CREATOR(ResourceHandler.getString("Tools", "connection_creator"), "edge-icon",
        Shortcuts.CONNECTION_CREATOR.get());

    private final String label;
    private final String icon;
    @SuppressWarnings("ImmutableEnumChecker")
    private final KeyCodeCombination keyCodeCombination;

    ToolType(String label, String icon, KeyCodeCombination keyCodeCombination) {
        this.label = label;
        this.icon = icon;
        this.keyCodeCombination = keyCodeCombination;
    }
}
