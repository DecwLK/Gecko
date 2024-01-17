package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.EditorView;

public class VariableBlockViewElementContextMenuBuilder extends AbstractContextMenuBuilder {

    public VariableBlockViewElementContextMenuBuilder(ActionManager actionManager, EditorView editorView) {
        super(actionManager, editorView);
    }

    @Override
    public ContextMenu build() {
        return null;
    }
}
