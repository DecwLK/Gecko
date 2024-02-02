package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.SeparatorMenuItem;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.EditorViewModel;

public class ViewContextMenuBuilder extends AbstractContextMenuBuilder {
    private final EditorViewModel editorViewModel;

    public ViewContextMenuBuilder(
        ActionManager actionManager, EditorView editorView, EditorViewModel editorViewModel) {
        super(actionManager, editorView);
        this.editorViewModel = editorViewModel;
    }

    @Override
    public ContextMenu build() {
        ContextMenu viewContextMenu = super.build();

        if (editorViewModel.getSelectionManager().getCurrentSelection().isEmpty()) {
            viewContextMenu.getItems().stream().filter(menuItem -> menuItem.getText().equals("Copy")).findAny()
                .ifPresent(copyMenuItem -> copyMenuItem.setDisable(true));
            viewContextMenu.getItems().stream().filter(menuItem -> menuItem.getText().equals("Cut")).findAny()
                .ifPresent(cutMenuItem -> cutMenuItem.setDisable(true));
            viewContextMenu.getItems().stream().filter(menuItem
                    -> !(menuItem instanceof SeparatorMenuItem) && menuItem.getText().equals("Deselect All")).findAny()
                .ifPresent(deselectMenuItem -> deselectMenuItem.setDisable(true));
        }

        return viewContextMenu;
    }
}