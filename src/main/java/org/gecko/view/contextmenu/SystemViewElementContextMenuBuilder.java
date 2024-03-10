package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.views.shortcuts.Shortcuts;
import org.gecko.viewmodel.SystemViewModel;

/**
 * Represents a type of {@link ViewContextMenuBuilder} for a {@link ContextMenu} specific to a
 * {@link org.gecko.view.views.viewelement.SystemViewElement SystemViewElement}. Contains {@link MenuItem}s that run
 * operations like opening or deleting the system.
 */
public class SystemViewElementContextMenuBuilder extends ViewContextMenuBuilder {

    private final SystemViewModel systemViewModel;

    public SystemViewElementContextMenuBuilder(
        ActionManager actionManager, SystemViewModel systemViewModel) {
        super(actionManager);

        this.systemViewModel = systemViewModel;
    }

    @Override
    public ContextMenu build() {
        ContextMenu systemContextMenu = super.build();

        SeparatorMenuItem dataTransferToSystemAccessSeparator = new SeparatorMenuItem();

        // Access system commands:
        MenuItem openSystemMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "open_system"));
        openSystemMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createViewSwitchAction(systemViewModel, false)));
        openSystemMenuItem.setAccelerator(Shortcuts.OPEN_CHILD_SYSTEM_EDITOR.get());

        MenuItem deleteMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "delete"));
        deleteMenuItem.setOnAction(e -> actionManager.run(
            actionManager.getActionFactory().createDeletePositionableViewModelElementAction(systemViewModel)));


        systemContextMenu.getItems().addAll(dataTransferToSystemAccessSeparator, openSystemMenuItem, deleteMenuItem);
        return systemContextMenu;
    }
}
