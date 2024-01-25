package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.EdgeViewModel;

public class EdgeViewElementContextMenuBuilder extends AbstractContextMenuBuilder {

    private final EdgeViewModel edgeViewModel;

    public EdgeViewElementContextMenuBuilder(
        ActionManager actionManager, EditorView editorView, EdgeViewModel edgeViewModel) {
        super(actionManager, editorView);

        this.edgeViewModel = edgeViewModel;
    }

    @Override
    public ContextMenu build() {
        return null;
    }
}
