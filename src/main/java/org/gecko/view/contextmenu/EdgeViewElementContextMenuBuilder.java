package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import org.gecko.actions.ActionManager;
import org.gecko.model.Kind;
import org.gecko.viewmodel.EdgeViewModel;

/**
 * Represents a type of {@link ViewContextMenuBuilder} for a {@link ContextMenu} specific to an
 * {@link org.gecko.view.views.viewelement.EdgeViewElement EdgeViewElement}. Contains {@link MenuItem}s that run
 * operations like changing the edge's kind or deleting the edge.
 */
public class EdgeViewElementContextMenuBuilder extends ViewContextMenuBuilder {

    private final EdgeViewModel edgeViewModel;

    private static final String CHANGE_KIND_MENU_ITEM = "Change Kind";
    private static final String HIT_MENU_ITEM = "HIT";
    private static final String MISS_KIND_MENU_ITEM = "MISS";
    private static final String FAIL_KIND_MENU_ITEM = "FAIL";

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
        Menu changeKindMenu = new Menu(CHANGE_KIND_MENU_ITEM); // TODO: Synchronize fields showed in inspector.

        MenuItem hitMenuItem = new MenuItem(HIT_MENU_ITEM);
        hitMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createChangeKindAction(edgeViewModel, Kind.HIT)));

        MenuItem missMenuItem = new MenuItem(MISS_KIND_MENU_ITEM);
        missMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createChangeKindAction(edgeViewModel, Kind.MISS)));

        MenuItem failMenuItem = new MenuItem(FAIL_KIND_MENU_ITEM);
        failMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createChangeKindAction(edgeViewModel, Kind.FAIL)));

        changeKindMenu.getItems().addAll(hitMenuItem, missMenuItem, failMenuItem);

        MenuItem deleteMenuItem = new MenuItem(DELETE_MENU_ITEM);
        deleteMenuItem.setOnAction(e -> actionManager.run(
            actionManager.getActionFactory().createDeletePositionableViewModelElementAction(edgeViewModel)));

        edgeContextMenu.getItems().addAll(dataTransferToEdgeEditingSeparator, changeKindMenu, deleteMenuItem);
        return edgeContextMenu;
    }
}
