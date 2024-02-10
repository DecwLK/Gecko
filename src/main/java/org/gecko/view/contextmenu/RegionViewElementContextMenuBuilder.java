package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.RegionViewModel;

public class RegionViewElementContextMenuBuilder extends ViewContextMenuBuilder {

    private final RegionViewModel regionViewModel;

    public RegionViewElementContextMenuBuilder(
        ActionManager actionManager, EditorViewModel editorViewModel, RegionViewModel regionViewModel) {
        super(actionManager, editorViewModel);

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
