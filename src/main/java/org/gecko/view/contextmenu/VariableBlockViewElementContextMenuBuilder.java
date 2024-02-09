package org.gecko.view.contextmenu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.PortViewModel;

public class VariableBlockViewElementContextMenuBuilder extends ViewContextMenuBuilder {

    private final PortViewModel portViewModel;

    public VariableBlockViewElementContextMenuBuilder(
        ActionManager actionManager, PortViewModel portViewModel) {
        super(actionManager);
        this.portViewModel = portViewModel;
    }

    @Override
    public ContextMenu build() {
        ContextMenu variableBlockContextMenu  = super.build();

        SeparatorMenuItem dataTransferToVariableBlockEditingSeparator = new SeparatorMenuItem();

        // Variable Block editing commands:
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createDeletePositionableViewModelElementAction(portViewModel)));

        variableBlockContextMenu.getItems().addAll(dataTransferToVariableBlockEditingSeparator, deleteMenuItem);
        return variableBlockContextMenu;
    }
}
