package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import org.gecko.actions.ActionManager;
import org.gecko.model.Kind;
import org.gecko.view.ResourceHandler;
import org.gecko.viewmodel.EdgeViewModel;

/**
 * Represents a type of {@link ViewContextMenuBuilder} for a {@link ContextMenu} specific to an
 * {@link org.gecko.view.views.viewelement.EdgeViewElement EdgeViewElement}. Contains {@link MenuItem}s that run
 * operations like changing the edge's kind or deleting the edge.
 */
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
        Menu changeKindMenu = new Menu(ResourceHandler.getString("Buttons", "change_kind"));

        MenuItem hitMenuItem = new MenuItem(Kind.HIT.name());
        hitMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createChangeKindAction(edgeViewModel, Kind.HIT)));

        MenuItem missMenuItem = new MenuItem(Kind.MISS.name());
        missMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createChangeKindAction(edgeViewModel, Kind.MISS)));

        MenuItem failMenuItem = new MenuItem(Kind.FAIL.name());
        failMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createChangeKindAction(edgeViewModel, Kind.FAIL)));

        changeKindMenu.getItems().addAll(hitMenuItem, missMenuItem, failMenuItem);

        MenuItem deleteMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "delete"));
        deleteMenuItem.setOnAction(e -> actionManager.run(
            actionManager.getActionFactory().createDeletePositionableViewModelElementAction(edgeViewModel)));

        edgeContextMenu.getItems().addAll(dataTransferToEdgeEditingSeparator, changeKindMenu, deleteMenuItem);
        return edgeContextMenu;
    }
}
