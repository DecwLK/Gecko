package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.PortViewModel;

public class VariableBlockViewElementContextMenuBuilder extends AbstractContextMenuBuilder {

    private final PortViewModel portViewModel;

    public VariableBlockViewElementContextMenuBuilder(
        ActionManager actionManager, EditorView editorView, PortViewModel portViewModel) {
        super(actionManager, editorView);
        this.portViewModel = portViewModel;
    }

    @Override
    public ContextMenu build() {
        return null;
    }
}
