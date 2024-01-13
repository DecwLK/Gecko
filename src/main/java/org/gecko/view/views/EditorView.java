package org.gecko.view.views;

import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import org.gecko.view.views.shortcuts.ShortcutHandler;
import org.gecko.viewmodel.EditorViewModel;

public class EditorView {
    private final EditorViewModel viewModel;
    private final ToolBar toolBar;
    private final ShortcutHandler shortcutHandler;

    public EditorView(EditorViewModel viewModel, ToolBar toolBar, ShortcutHandler shortcutHandler) {
        this.viewModel = viewModel;
        this.toolBar = toolBar;
        this.shortcutHandler = shortcutHandler;
    }

    public void toggleInspector() {
    }

    public Node drawView() {
        return null;
    }

    public Node drawToolbar() {
        return null;
    }

    public Node drawInspector() {
        return null;
    }
}
