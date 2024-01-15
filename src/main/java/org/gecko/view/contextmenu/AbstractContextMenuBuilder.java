package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;

import org.gecko.view.views.EditorView;

public abstract class AbstractContextMenuBuilder {

    protected final EditorView editorView;

    protected AbstractContextMenuBuilder(EditorView editorView) {
        this.editorView = editorView;
    }

    public abstract ContextMenu build();
}
