package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.EditorView;

public class RegionViewElementContextMenuBuilder extends AbstractContextMenuBuilder {

    public RegionViewElementContextMenuBuilder(ActionManager actionManager, EditorView editorView) {
        super(actionManager, editorView);
    }

    @Override
    public ContextMenu build() {
        return null;
    }
}
