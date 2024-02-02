package org.gecko.view.views.shortcuts;

import java.util.List;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import org.gecko.actions.ActionManager;
import org.gecko.tools.ToolType;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.SystemViewModel;

public class SystemEditorViewShortcutHandler extends ShortcutHandler {

    public SystemEditorViewShortcutHandler(ActionManager actionManager, EditorView editorView) {
        super(actionManager, editorView);

        addNavigateSystemShortcuts();
        addCreatorShortcuts();
    }

    private void addNavigateSystemShortcuts() {
        KeyCodeCombination openSystem =
            new KeyCodeCombination(KeyCode.O, KeyCodeCombination.SHORTCUT_DOWN, KeyCodeCombination.SHIFT_DOWN);
        shortcuts.put(openSystem, () -> {
            PositionableViewModelElement<?> focusedElement = editorView.getViewModel().getFocusedElement();
            if (focusedElement == null) {
                return;
            }
            try {
                SystemViewModel systemViewModel = (SystemViewModel) focusedElement;
                actionManager.run(actionFactory.createViewSwitchAction(systemViewModel, false));
            } catch (ClassCastException e) {
                return;
            }
        });

        KeyCodeCombination goToParentSystem =
            new KeyCodeCombination(KeyCode.P, KeyCodeCombination.SHORTCUT_DOWN, KeyCodeCombination.SHIFT_DOWN);
        shortcuts.put(goToParentSystem, () -> {
            SystemViewModel parentSystem = editorView.getViewModel().getParentSystem();
            if (parentSystem == null) {
                return;
            }
            actionManager.run(actionFactory.createViewSwitchAction(parentSystem, false));
        });
    }

    private void addCreatorShortcuts() {
        List<ToolType> creatorTools = List.of(ToolType.SYSTEM_CREATOR_TOOL, ToolType.CONNECTION_CREATOR_TOOL,
            ToolType.VARIABLE_BLOCK_CREATOR_TOOL);
        creatorTools.forEach(tool -> {
            KeyCodeCombination keyCodeCombination = new KeyCodeCombination(tool.getKeyCode());
            shortcuts.put(keyCodeCombination,
                () -> actionManager.run(actionFactory.createSelectToolAction(editorView, tool)));
        });
    }
}

