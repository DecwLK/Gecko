package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.SystemConnectionViewModel;

public class SystemConnectionViewElementContextMenuBuilder extends AbstractContextMenuBuilder {

    private final SystemConnectionViewModel systemConnectionViewModel;

    public SystemConnectionViewElementContextMenuBuilder(
        ActionManager actionManager, EditorView editorView, SystemConnectionViewModel systemConnectionViewModel) {
        super(actionManager, editorView);

        this.systemConnectionViewModel = systemConnectionViewModel;
    }

    @Override
    public ContextMenu build() {
        return null;
    }
}
