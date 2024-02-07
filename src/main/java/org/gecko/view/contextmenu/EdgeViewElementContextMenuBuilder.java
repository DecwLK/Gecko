package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import org.gecko.actions.ActionManager;
import org.gecko.model.Kind;
import org.gecko.viewmodel.EdgeViewModel;

public class EdgeViewElementContextMenuBuilder extends ViewContextMenuBuilder {

    private final EdgeViewModel edgeViewModel;

    public EdgeViewElementContextMenuBuilder(
        ActionManager actionManager, EdgeViewModel edgeViewModel) {
        super(actionManager);

        this.edgeViewModel = edgeViewModel;
    }

    @Override
    public ContextMenu build() {
        ContextMenu edgeContextMenu = super.build();

        SeparatorMenuItem dataTransferToEdgeEditingSeparator = new SeparatorMenuItem();

        // Edge editing commands:
        Menu changeKindMenu = new Menu("Change Kind"); // TODO: Synchronize fields showed in inspector.

        MenuItem hitMenuItem = new MenuItem("HIT");
        hitMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createChangeKindAction(edgeViewModel, Kind.HIT)));

        MenuItem missMenuItem = new MenuItem("MISS");
        missMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createChangeKindAction(edgeViewModel, Kind.MISS)));

        MenuItem failMenuItem = new MenuItem("FAIL");
        failMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createChangeKindAction(edgeViewModel, Kind.FAIL)));

        changeKindMenu.getItems().addAll(hitMenuItem, missMenuItem, failMenuItem);

        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createDeletePositionableViewModelElementAction(edgeViewModel)));

        edgeContextMenu.getItems().addAll(dataTransferToEdgeEditingSeparator, changeKindMenu, deleteMenuItem);
        return edgeContextMenu;
    }
}
