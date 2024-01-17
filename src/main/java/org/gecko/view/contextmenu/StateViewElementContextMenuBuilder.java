package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.EditorView;

public class StateViewElementContextMenuBuilder extends AbstractContextMenuBuilder {

    public StateViewElementContextMenuBuilder(ActionManager actionManager, EditorView editorView) {
        super(actionManager, editorView);
    }

    @Override
    public ContextMenu build() {
        return null;
    }
}
