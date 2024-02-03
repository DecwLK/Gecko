package org.gecko.view.views.shortcuts;

import java.util.List;
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
        shortcuts.put(Shortcuts.OPEN_CHILD_SYSTEM_EDITOR.get(), () -> {
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

        shortcuts.put(Shortcuts.OPEN_PARENT_SYSTEM_EDITOR.get(), () -> {
            SystemViewModel parentSystem = editorView.getViewModel().getParentSystem();
            if (parentSystem == null) {
                return;
            }
            actionManager.run(actionFactory.createViewSwitchAction(parentSystem, false));
        });
    }

    private void addCreatorShortcuts() {
        List<ToolType> creatorTools =
            List.of(ToolType.SYSTEM_CREATOR, ToolType.CONNECTION_CREATOR, ToolType.VARIABLE_BLOCK_CREATOR);
        creatorTools.forEach(tool -> {
            shortcuts.put(tool.getKeyCodeCombination(),
                () -> actionManager.run(actionFactory.createSelectToolAction(tool)));
        });
    }
}

