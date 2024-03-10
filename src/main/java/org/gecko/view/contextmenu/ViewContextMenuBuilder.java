package org.gecko.view.contextmenu;

import javafx.beans.binding.Bindings;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import lombok.Getter;
import lombok.Setter;
import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.views.shortcuts.Shortcuts;
import org.gecko.viewmodel.EditorViewModel;

/**
 * Represents a builder for a general purpose {@link ContextMenu} in the view, containing {@link MenuItem}s that run the
 * cut, copy, paste, select all and deselect all operations. Holds therefore a reference to the {@link ActionManager}
 * and to the built {@link ContextMenu}.
 */
public class ViewContextMenuBuilder {
    protected final ActionManager actionManager;
    @Setter
    protected EditorViewModel editorViewModel;
    @Getter
    protected ContextMenu contextMenu;

    public ViewContextMenuBuilder(ActionManager actionManager) {
        this.actionManager = actionManager;
        this.editorViewModel = null;
    }

    public ViewContextMenuBuilder(ActionManager actionManager, EditorViewModel editorViewModel) {
        this.actionManager = actionManager;
        this.editorViewModel = editorViewModel;
    }

    public ContextMenu build() {
        ContextMenu contextMenu = new ContextMenu();

        // Data transfer commands:
        MenuItem cutMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "cut"));
        cutMenuItem.setOnAction(e -> {
            actionManager.run(actionManager.getActionFactory().createCopyPositionableViewModelElementAction());
            actionManager.run(actionManager.getActionFactory().createDeletePositionableViewModelElementAction());
        });
        cutMenuItem.setAccelerator(Shortcuts.CUT.get());
        if (editorViewModel != null) {
            cutMenuItem.disableProperty()
                .bind(Bindings.createBooleanBinding(
                    () -> editorViewModel.getSelectionManager().getCurrentSelection().isEmpty(),
                    editorViewModel.getSelectionManager().getCurrentSelectionProperty()));
        }

        MenuItem copyMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "copy"));
        copyMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createCopyPositionableViewModelElementAction()));
        copyMenuItem.setAccelerator(Shortcuts.COPY.get());
        if (editorViewModel != null) {
            copyMenuItem.disableProperty()
                .bind(Bindings.createBooleanBinding(
                    () -> editorViewModel.getSelectionManager().getCurrentSelection().isEmpty(),
                    editorViewModel.getSelectionManager().getCurrentSelectionProperty()));
        }

        MenuItem pasteMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "paste"));
        pasteMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createPastePositionableViewModelElementAction()));
        pasteMenuItem.setAccelerator(Shortcuts.PASTE.get());

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        separatorMenuItem.setText("");

        MenuItem selectMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "select_all"));
        selectMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectAction(editorViewModel.getPositionableViewModelElements(), true)));
        selectMenuItem.setAccelerator(Shortcuts.SELECT_ALL.get());
        if (editorViewModel != null) {
            selectMenuItem.disableProperty()
                .bind(Bindings.createBooleanBinding(
                    () -> editorViewModel.getContainedPositionableViewModelElementsProperty().isEmpty(),
                    editorViewModel.getContainedPositionableViewModelElementsProperty()));
        }

        MenuItem deselectMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "deselect_all"));
        deselectMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory().createDeselectAction()));
        deselectMenuItem.setAccelerator(Shortcuts.DESELECT_ALL.get());
        if (editorViewModel != null) {
            deselectMenuItem.disableProperty()
                .bind(Bindings.createBooleanBinding(
                    () -> editorViewModel.getSelectionManager().getCurrentSelection().isEmpty(),
                    editorViewModel.getSelectionManager().getCurrentSelectionProperty()));
        }

        contextMenu.getItems()
            .addAll(cutMenuItem, copyMenuItem, pasteMenuItem, separatorMenuItem, selectMenuItem, deselectMenuItem);

        this.contextMenu = contextMenu;
        return contextMenu;
    }
}
