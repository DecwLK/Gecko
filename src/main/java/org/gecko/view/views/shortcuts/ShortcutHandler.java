package org.gecko.view.views.shortcuts;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javafx.event.EventHandler;
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
            List.of(ToolType.CURSOR, ToolType.MARQUEE_TOOL, ToolType.PAN, ToolType.ZOOM_TOOL);
        standardTools.forEach(tool -> {
            shortcuts.put(tool.getKeyCodeCombination(),
                () -> actionManager.run(actionFactory.createSelectToolAction(editorView, tool)));
        });
    }

    private void addZoomShortcuts() {
        shortcuts.put(Shortcuts.ZOOM_IN.get(), () -> {
            actionManager.run(actionFactory.createZoomCenterAction(ZOOM_FACTOR));
        });
        shortcuts.put(Shortcuts.ZOOM_OUT.get(), () -> {
            actionManager.run(actionFactory.createZoomCenterAction(1 / ZOOM_FACTOR));
        });
    }

    private void addSelectionShortcuts() {
        shortcuts.put(Shortcuts.SELECT_ALL.get(), () -> {
            actionManager.run(
                actionFactory.createSelectAction(editorView.getViewModel().getPositionableViewModelElements(), true));
        });

        shortcuts.put(Shortcuts.DESELECT_ALL.get(), () -> {
            actionManager.run(actionFactory.createSelectAction(Set.of(), true));
        });

        shortcuts.put(Shortcuts.SELECTION_FORWARD.get(), () -> {
            actionManager.run(actionFactory.createSelectionHistoryForwardAction());
        });

        shortcuts.put(Shortcuts.SELECTION_BACK.get(), () -> {
            actionManager.run(actionFactory.createSelectionHistoryBackAction());
        });
    }

    private void addDeleteShortcuts() {
        shortcuts.put(Shortcuts.DELETE.get(), () -> {
            actionManager.run(actionFactory.createDeletePositionableViewModelElementAction(
                editorView.getViewModel().getSelectionManager().getCurrentSelection()));
        });
    }

    private void addUndoRedoShortcuts() {
        shortcuts.put(Shortcuts.UNDO.get(), actionManager::undo);

        shortcuts.put(Shortcuts.REDO.get(), actionManager::redo);
    }

    private void addSwitchEditorShortcuts() {
        shortcuts.put(Shortcuts.SWITCH_EDITOR.get(), () -> {
            EditorViewModel editorViewModel = editorView.getViewModel();
            actionManager.run(actionFactory.createViewSwitchAction(editorViewModel.getCurrentSystem(),
                !editorViewModel.isAutomatonEditor()));
        });
    }
}
