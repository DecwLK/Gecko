package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.StateViewModel;

public class StateViewElementContextMenuBuilder extends AbstractContextMenuBuilder {

    private final StateViewModel stateViewModel;

    public StateViewElementContextMenuBuilder(ActionManager actionManager, EditorView editorView, StateViewModel stateViewModel) {
        super(actionManager, editorView);
        this.stateViewModel = stateViewModel;
    }

    @Override
    public ContextMenu build() {
        return null;
    }
}
