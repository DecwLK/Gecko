package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import org.gecko.actions.ActionManager;
import org.gecko.model.Kind;
import org.gecko.view.GeckoView;
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
        ActionManager actionManager, EdgeViewModel edgeViewModel, GeckoView geckoView) {
        super(actionManager, geckoView);

        this.edgeViewModel = edgeViewModel;
    }

    @Override
    public ContextMenu build() {
        ContextMenu edgeContextMenu = super.build();

        SeparatorMenuItem dataTransferToEdgeEditingSeparator = new SeparatorMenuItem();

        // Edge editing commands:
        Menu changeKindMenu = new Menu(ResourceHandler.getString("Buttons", "change_kind"));

        for (Kind kind : Kind.values()) {
            MenuItem kindMenuItem = createKindMenuItem(kind);

            if (edgeViewModel.getKind() == kind) {
                kindMenuItem.setDisable(true);
            }

            changeKindMenu.getItems().add(kindMenuItem);
        }

        MenuItem deleteMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "delete"));
        deleteMenuItem.setOnAction(e -> actionManager.run(
            actionManager.getActionFactory().createDeletePositionableViewModelElementAction(edgeViewModel)));

        edgeContextMenu.getItems().addAll(dataTransferToEdgeEditingSeparator, changeKindMenu, deleteMenuItem);
        return edgeContextMenu;
    }

    private MenuItem createKindMenuItem(Kind kind) {
        MenuItem kindMenuItem = new MenuItem(kind.name());
        kindMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createChangeKindAction(edgeViewModel, kind)));
        return kindMenuItem;
    }
}
