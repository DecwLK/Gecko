package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import lombok.Getter;
import org.gecko.actions.ActionManager;
import org.gecko.actions.CopyPositionableViewModelElementAction;
import org.gecko.view.views.EditorView;
import org.gecko.view.views.shortcuts.Shortcuts;

public abstract class AbstractContextMenuBuilder {

    protected final EditorView editorView;
    protected final ActionManager actionManager;
    @Getter
    protected ContextMenu contextMenu;

    protected AbstractContextMenuBuilder(ActionManager actionManager, EditorView editorView) {
        this.actionManager = actionManager;
        this.editorView = editorView;
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

        MenuItem deselectMenuItem = new MenuItem("Deselect All");
        deselectMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory().createDeselectAction()));
        deselectMenuItem.setAccelerator(Shortcuts.DESELECT_ALL.get());

        contextMenu.getItems().addAll(cutMenuItem, copyMenuItem, pasteMenuItem, separatorMenuItem, deselectMenuItem);

        this.contextMenu = contextMenu;
        return contextMenu;
    }
}
