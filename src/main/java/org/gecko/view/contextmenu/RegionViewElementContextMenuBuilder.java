package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.RegionViewModel;

/**
 * Represents a type of {@link ViewContextMenuBuilder} for a {@link ContextMenu} specific to a
 * {@link org.gecko.view.views.viewelement.RegionViewElement RegionViewElement}. Contains {@link MenuItem}s that run
 * operations like deleting the edge.
 */
public class RegionViewElementContextMenuBuilder extends ViewContextMenuBuilder {

    private final RegionViewModel regionViewModel;

    public RegionViewElementContextMenuBuilder(
        ActionManager actionManager, RegionViewModel regionViewModel) {
        super(actionManager);

        this.regionViewModel = regionViewModel;
    }

    @Override
    public ContextMenu build() {
        ContextMenu regionContextMenu = super.build();

        SeparatorMenuItem dataTransferToRegionEditingSeparator = new SeparatorMenuItem();

        // Region editing commands:
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(e -> actionManager.run(
            actionManager.getActionFactory().createDeletePositionableViewModelElementAction(regionViewModel)));

        regionContextMenu.getItems().addAll(dataTransferToRegionEditingSeparator, deleteMenuItem);
        return regionContextMenu;
    }
}
