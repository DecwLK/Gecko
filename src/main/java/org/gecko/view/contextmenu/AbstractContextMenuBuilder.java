package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.EditorView;

public abstract class AbstractContextMenuBuilder {

    protected final EditorView editorView;
    protected final ActionManager actionManager;

    protected AbstractContextMenuBuilder(ActionManager actionManager, EditorView editorView) {
        this.actionManager = actionManager;
        this.editorView = editorView;
    }

    public ContextMenu build() {
        ContextMenu contextMenu = new ContextMenu();

        // Data transfer commands:
        MenuItem cutMenuItem = new MenuItem("Cut");
        MenuItem copyMenuItem = new MenuItem("Copy");
        MenuItem pasteMenuItem = new MenuItem("Paste");

        contextMenu.getItems().addAll(cutMenuItem, copyMenuItem, pasteMenuItem);
        return contextMenu;
    }
}
