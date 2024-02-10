package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import lombok.Getter;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.shortcuts.Shortcuts;
import org.gecko.viewmodel.EditorViewModel;

public class ViewContextMenuBuilder {
    protected final ActionManager actionManager;
    protected final EditorViewModel editorViewModel;
    @Getter
    protected ContextMenu contextMenu;

    public ViewContextMenuBuilder(ActionManager actionManager, EditorViewModel editorViewModel) {
        this.actionManager = actionManager;
        this.editorViewModel = editorViewModel;
    }

    public ContextMenu build() {
        ContextMenu contextMenu = new ContextMenu();

        // Data transfer commands:
        MenuItem cutMenuItem = new MenuItem("Cut");
        cutMenuItem.setOnAction(e -> actionManager.cut());
        cutMenuItem.setAccelerator(Shortcuts.CUT.get());

        MenuItem copyMenuItem = new MenuItem("Copy");
        copyMenuItem.setOnAction(e -> actionManager.copy());
        copyMenuItem.setAccelerator(Shortcuts.COPY.get());

        MenuItem pasteMenuItem = new MenuItem("Paste");
        pasteMenuItem.setOnAction(e -> actionManager.paste());
        pasteMenuItem.setAccelerator(Shortcuts.PASTE.get());

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        separatorMenuItem.setText("");

        MenuItem selectMenuItem = new MenuItem("Select All");
        selectMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectAction(editorViewModel.getPositionableViewModelElements(), true)));
        selectMenuItem.setAccelerator(Shortcuts.SELECT_ALL.get());

        MenuItem deselectMenuItem = new MenuItem("Deselect All");
        deselectMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory().createDeselectAction()));
        deselectMenuItem.setAccelerator(Shortcuts.DESELECT_ALL.get());

        contextMenu.getItems().addAll(cutMenuItem, copyMenuItem, pasteMenuItem, separatorMenuItem, selectMenuItem,
            deselectMenuItem);

        this.contextMenu = contextMenu;
        return contextMenu;
    }
}
