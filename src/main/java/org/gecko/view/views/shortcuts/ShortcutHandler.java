package org.gecko.view.views.shortcuts;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import org.gecko.actions.ActionFactory;
import org.gecko.actions.ActionManager;
import org.gecko.tools.ToolType;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.EditorViewModel;

public abstract class ShortcutHandler implements EventHandler<KeyEvent> {
    private static final double ZOOM_FACTOR = 1.1;
    protected HashMap<KeyCodeCombination, Runnable> shortcuts = new HashMap<>();
    protected ActionManager actionManager;
    protected ActionFactory actionFactory;
    protected EditorView editorView;

    protected ShortcutHandler(ActionManager actionManager, EditorView editorView) {
        this.actionManager = actionManager;
        this.actionFactory = actionManager.getActionFactory();
        this.editorView = editorView;

        addSelectStandardToolShortcuts();
        addZoomShortcuts();
        addSelectionShortcuts();
        addDeleteShortcuts();
        addUndoRedoShortcuts();
        addSwitchEditorShortcuts();
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            Runnable shortcutAction = getShortcutAction(event);
            if (shortcutAction != null) {
                shortcutAction.run();
                event.consume();
            }
        }
    }

    private Runnable getShortcutAction(KeyEvent event) {
        Optional<KeyCodeCombination> keyCodeCombination =
            shortcuts.keySet().stream().filter(shortcut -> shortcut.match(event)).findFirst();
        return keyCodeCombination.map(codeCombination -> shortcuts.get(codeCombination)).orElse(null);
    }

    private void addSelectStandardToolShortcuts() {
        List<ToolType> standardTools =
            List.of(ToolType.CURSOR_TOOL, ToolType.MARQUEE_TOOL, ToolType.PAN_TOOL, ToolType.ZOOM_TOOL);
        standardTools.forEach(tool -> {
            KeyCodeCombination keyCodeCombination = new KeyCodeCombination(tool.getKeyCode());
            shortcuts.put(keyCodeCombination,
                () -> actionManager.run(actionFactory.createSelectToolAction(editorView, tool)));
        });
    }

    private void addZoomShortcuts() {
        KeyCodeCombination zoomIn = new KeyCodeCombination(KeyCode.PLUS, KeyCodeCombination.SHORTCUT_DOWN);
        shortcuts.put(zoomIn, () -> {
            actionManager.run(actionFactory.createZoomCenterAction(ZOOM_FACTOR));
        });
        KeyCodeCombination zoomOut = new KeyCodeCombination(KeyCode.MINUS, KeyCodeCombination.SHORTCUT_DOWN);
        shortcuts.put(zoomOut, () -> {
            actionManager.run(actionFactory.createZoomCenterAction(1 / ZOOM_FACTOR));
        });
    }

    private void addSelectionShortcuts() {
        KeyCodeCombination selectAll = new KeyCodeCombination(KeyCode.A, KeyCodeCombination.SHORTCUT_DOWN);
        shortcuts.put(selectAll, () -> {
            actionManager.run(
                actionFactory.createSelectAction(editorView.getViewModel().getPositionableViewModelElements(), true));
        });

        KeyCodeCombination deselectAll =
            new KeyCodeCombination(KeyCode.A, KeyCodeCombination.SHORTCUT_DOWN, KeyCodeCombination.SHIFT_DOWN);
        shortcuts.put(deselectAll, () -> {
            actionManager.run(actionFactory.createSelectAction(Set.of(), true));
        });

        KeyCodeCombination selectionForward =
            new KeyCodeCombination(KeyCode.U, KeyCodeCombination.SHORTCUT_DOWN, KeyCodeCombination.SHIFT_DOWN);
        shortcuts.put(selectionForward, () -> {
            actionManager.run(actionFactory.createSelectionHistoryForwardAction());
        });

        KeyCodeCombination selectionBack = new KeyCodeCombination(KeyCode.U, KeyCodeCombination.SHORTCUT_DOWN);
        shortcuts.put(selectionBack, () -> {
            actionManager.run(actionFactory.createSelectionHistoryBackAction());
        });
    }

    private void addDeleteShortcuts() {
        KeyCodeCombination delete = new KeyCodeCombination(KeyCode.BACK_SPACE, KeyCodeCombination.SHORTCUT_DOWN);
        shortcuts.put(delete, () -> {
            actionManager.run(actionFactory.createDeletePositionableViewModelElementAction(
                editorView.getViewModel().getSelectionManager().getCurrentSelection()));
        });
    }

    private void addUndoRedoShortcuts() {
        KeyCodeCombination undo = new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.SHORTCUT_DOWN);
        shortcuts.put(undo, actionManager::undo);

        KeyCodeCombination redo =
            new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.SHORTCUT_DOWN, KeyCodeCombination.SHIFT_DOWN);
        shortcuts.put(redo, actionManager::redo);
    }

    private void addSwitchEditorShortcuts() {
        KeyCodeCombination switchEditor = new KeyCodeCombination(KeyCode.F1, KeyCodeCombination.SHORTCUT_DOWN);
        shortcuts.put(switchEditor, () -> {
            EditorViewModel editorViewModel = editorView.getViewModel();
            actionManager.run(actionFactory.createViewSwitchAction(editorViewModel.getCurrentSystem(),
                !editorViewModel.isAutomatonEditor()));
        });
    }
}
