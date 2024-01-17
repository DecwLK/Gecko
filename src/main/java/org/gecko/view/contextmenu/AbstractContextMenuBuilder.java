package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.EditorView;

public abstract class AbstractContextMenuBuilder {

    protected final EditorView editorView;
    protected final ActionManager actionManager;

    protected AbstractContextMenuBuilder(ActionManager actionManager, EditorView editorView) {
        this.actionManager = actionManager;
        this.editorView = editorView;
    }

    public abstract ContextMenu build();
}
