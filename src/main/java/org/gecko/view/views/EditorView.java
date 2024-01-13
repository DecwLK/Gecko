package org.gecko.view.views;

import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import org.gecko.view.toolbar.ToolBarBuilder;
import org.gecko.view.views.shortcuts.ShortcutHandler;
import org.gecko.viewmodel.EditorViewModel;

public class EditorView {
    private final EditorViewModel viewModel;
    private final ToolBarBuilder toolBarBuilder; //TODO needed?
    private final ToolBar toolBar;
    private final ShortcutHandler shortcutHandler;

    public EditorView(EditorViewModel viewModel) {
        this.viewModel = viewModel;
        this.toolBarBuilder = new ToolBarBuilder(this);
        this.toolBar = toolBarBuilder.build();
        this.shortcutHandler = new ShortcutHandler();
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
