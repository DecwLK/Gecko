package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.SystemViewModel;

public class SystemViewElementContextMenuBuilder extends AbstractContextMenuBuilder {

    private final SystemViewModel systemViewModel;

    public SystemViewElementContextMenuBuilder(ActionManager actionManager, EditorView editorView, SystemViewModel systemViewModel) {
        super(actionManager, editorView);

        this.systemViewModel = systemViewModel;
    }

    @Override
    public ContextMenu build() {
        return null;
    }
}
