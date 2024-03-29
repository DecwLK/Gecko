package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import org.gecko.actions.ActionManager;
import org.gecko.view.GeckoView;
import org.gecko.view.ResourceHandler;
import org.gecko.viewmodel.StateViewModel;

/**
 * Represents a type of {@link ViewContextMenuBuilder} for a {@link ContextMenu} specific to a
 * {@link org.gecko.view.views.viewelement.StateViewElement StateViewElement}. Contains {@link MenuItem}s that run
 * operations like setting the state as start-state or deleting the state.
 */
public class StateViewElementContextMenuBuilder extends ViewContextMenuBuilder {

    private final StateViewModel stateViewModel;

    public StateViewElementContextMenuBuilder(
        ActionManager actionManager, StateViewModel stateViewModel, GeckoView geckoView) {
        super(actionManager, geckoView);
        this.stateViewModel = stateViewModel;
    }

    @Override
    public ContextMenu build() {
        ContextMenu stateContextMenu = super.build();

        SeparatorMenuItem dataTransferToStateEditingSeparator = new SeparatorMenuItem();

        // State editing commands:
        MenuItem startStateMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "set_start_state"));
        startStateMenuItem.setDisable(stateViewModel.getIsStartState());
        startStateMenuItem.setOnAction(e -> actionManager.run(
            actionManager.getActionFactory().createSetStartStateViewModelElementAction(stateViewModel)));

        MenuItem deleteMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "delete"));
        deleteMenuItem.setOnAction(e -> actionManager.run(
            actionManager.getActionFactory().createDeletePositionableViewModelElementAction(stateViewModel)));

        stateContextMenu.getItems().addAll(dataTransferToStateEditingSeparator, startStateMenuItem, deleteMenuItem);
        return stateContextMenu;
    }
}
