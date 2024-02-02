package org.gecko.view.views.shortcuts;

import java.util.List;
import javafx.scene.input.KeyCodeCombination;
import org.gecko.actions.ActionManager;
import org.gecko.tools.ToolType;
import org.gecko.view.views.EditorView;

public class AutomatonEditorViewShortcutHandler extends ShortcutHandler {
    public AutomatonEditorViewShortcutHandler(ActionManager actionManager, EditorView editorView) {
        super(actionManager, editorView);

        addCreatorShortcuts();
    }

    private void addCreatorShortcuts() {
        List<ToolType> creatorTools =
            List.of(ToolType.STATE_CREATOR_TOOL, ToolType.EDGE_CREATOR_TOOL, ToolType.REGION_CREATOR_TOOL);
        creatorTools.forEach(tool -> {
            KeyCodeCombination keyCodeCombination = new KeyCodeCombination(tool.getKeyCode());
            shortcuts.put(keyCodeCombination,
                () -> actionManager.run(actionFactory.createSelectToolAction(editorView, tool)));
        });
    }
}
