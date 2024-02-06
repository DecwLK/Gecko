package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.EditorView;
import org.gecko.view.views.shortcuts.Shortcuts;
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
        ContextMenu systemContextMenu = super.build();

        SeparatorMenuItem dataTransferToSystemAccessSeparator = new SeparatorMenuItem();

        // Access system commands:
        MenuItem openSystemMenuItem = new MenuItem("Open System");
        openSystemMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createViewSwitchAction(systemViewModel, false)));
        openSystemMenuItem.setAccelerator(Shortcuts.OPEN_CHILD_SYSTEM_EDITOR.get());

        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createDeletePositionableViewModelElementAction(systemViewModel)));


        systemContextMenu.getItems().addAll(dataTransferToSystemAccessSeparator, openSystemMenuItem, deleteMenuItem);
        return systemContextMenu;
    }
}
