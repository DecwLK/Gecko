package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.SystemViewModel;

public class SystemViewElementContextMenuBuilder extends AbstractContextMenuBuilder {

    private final SystemViewModel systemViewModel;

    public SystemViewElementContextMenuBuilder(
        ActionManager actionManager, EditorView editorView, SystemViewModel systemViewModel) {
        super(actionManager, editorView);

        this.systemViewModel = systemViewModel;
    }

    @Override
    public ContextMenu build() {
        ContextMenu systemContextMenu = new ContextMenu();

        // Data transfer commands:
        MenuItem cutMenuItem = new MenuItem("Cut");
        MenuItem copyMenuItem = new MenuItem("Copy");
        MenuItem pasteMenuItem = new MenuItem("Paste");

        SeparatorMenuItem dataTransferToSystemAccessSeparator = new SeparatorMenuItem();

        // Access system commands:
        MenuItem openSystemMenuItem = new MenuItem("Open System");
        openSystemMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createViewSwitchAction(systemViewModel, false)));

        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createDeletePositionableViewModelElementAction(systemViewModel)));


        systemContextMenu.getItems().addAll(cutMenuItem, copyMenuItem, pasteMenuItem,
            dataTransferToSystemAccessSeparator, openSystemMenuItem, deleteMenuItem);
        return systemContextMenu;
    }
}
