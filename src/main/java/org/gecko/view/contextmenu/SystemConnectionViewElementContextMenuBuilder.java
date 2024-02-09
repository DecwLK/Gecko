package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.SystemConnectionViewModel;

public class SystemConnectionViewElementContextMenuBuilder extends ViewContextMenuBuilder {

    private final SystemConnectionViewModel systemConnectionViewModel;

    public SystemConnectionViewElementContextMenuBuilder(
        ActionManager actionManager, SystemConnectionViewModel systemConnectionViewModel) {
        super(actionManager);

        this.systemConnectionViewModel = systemConnectionViewModel;
    }

    @Override
    public ContextMenu build() {
        ContextMenu systemConnectionContextMenu = super.build();

        SeparatorMenuItem dataTransferToEdgeEditingSeparator = new SeparatorMenuItem();

        // SystemConnection editing commands:
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createDeletePositionableViewModelElementAction(systemConnectionViewModel)));

        systemConnectionContextMenu.getItems().addAll(dataTransferToEdgeEditingSeparator, deleteMenuItem);
        return systemConnectionContextMenu;
    }
}
