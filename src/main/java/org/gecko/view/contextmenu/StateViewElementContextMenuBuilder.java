package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.StateViewModel;

public class StateViewElementContextMenuBuilder extends AbstractContextMenuBuilder {

    private final StateViewModel stateViewModel;

    public StateViewElementContextMenuBuilder(
        ActionManager actionManager, EditorView editorView, StateViewModel stateViewModel) {
        super(actionManager, editorView);
        this.stateViewModel = stateViewModel;
    }

    @Override
    public ContextMenu build() {
        ContextMenu stateContextMenu  = super.build();

        SeparatorMenuItem dataTransferToStateEditingSeparator = new SeparatorMenuItem();

        // State editing commands:
        MenuItem startStateMenuItem = new MenuItem("Start State");
        startStateMenuItem.setDisable(stateViewModel.getIsStartState());
        startStateMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSetStartStateViewModelElementAction(stateViewModel)));

        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createDeletePositionableViewModelElementAction(stateViewModel)));

        stateContextMenu.getItems().addAll(dataTransferToStateEditingSeparator, startStateMenuItem, deleteMenuItem);
        return stateContextMenu;
    }
}
