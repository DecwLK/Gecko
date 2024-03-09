package org.gecko.view.views.shortcuts;

import java.util.List;
import org.gecko.actions.ActionManager;
import org.gecko.tools.ToolType;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.SystemViewModel;

/**
 * A concrete representation of a {@link ShortcutHandler} that manages the shortcuts corresponding to the navigation of
 * and the selection of creator tools specific to a system editor view.
 */
public class SystemEditorViewShortcutHandler extends ShortcutHandler {

    public SystemEditorViewShortcutHandler(ActionManager actionManager, EditorView editorView) {
        super(actionManager, editorView);

        addNavigateSystemShortcuts();
        addCreatorShortcuts();
    }

    @SuppressWarnings("CatchAndPrintStackTrace")
    private void addNavigateSystemShortcuts() {
        shortcuts.put(Shortcuts.OPEN_CHILD_SYSTEM_EDITOR.get(), () -> {
            PositionableViewModelElement<?> focusedElement = editorView.getViewModel().getFocusedElement();
            if (focusedElement == null) {
                return;
            }
            SystemViewModel systemViewModel = (SystemViewModel) focusedElement;
            actionManager.run(actionFactory.createViewSwitchAction(systemViewModel, false));
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

