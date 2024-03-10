package org.gecko.view.menubar;

import java.io.File;
import java.util.Set;
import javafx.beans.binding.Bindings;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import org.gecko.actions.ActionManager;
import org.gecko.application.GeckoIOManager;
import org.gecko.io.FileTypes;
import org.gecko.tools.ToolType;
import org.gecko.view.GeckoView;
import org.gecko.view.ResourceHandler;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.view.inspector.element.textfield.InspectorRenameField;
import org.gecko.view.views.shortcuts.Shortcuts;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.Renamable;
import org.gecko.viewmodel.SystemViewModel;

/**
 * Represents a builder for the {@link MenuBar} displayed in the view, containing {@link MenuItem}s in {@link Menu}s
 * grouped by category. Holds a reference to the built {@link MenuBar}, the current {@link GeckoView} and the
 * {@link ActionManager}, which allow for actions to be run from the menu bar. Relevant menus for the Gecko Graphic
 * Editor are "File" (running operations like creating, saving, loading, importing and exporting files), "Edit" (running
 * operations like undoing and redoing actions, cutting, copying and pasting or selecting and deselecting all elements),
 * "View" (running operations like changing the view, opening the parent system or zooming in and out of the view),
 * "Tools" (providing the active tools which can be selected in the current view) and "Help" (running operations like
 * finding an element by name matches, opening a comprehensive list of all shortcuts available or reading more
 * information about Gecko).
 */
public class MenuBarBuilder {
    private final MenuBar menuBar;
    private final GeckoView view;
    private final ActionManager actionManager;

    public MenuBarBuilder(GeckoView view, ActionManager actionManager) {
        this.view = view;
        this.actionManager = actionManager;
        menuBar = new MenuBar();

        menuBar.getMenus().addAll(setupFileMenu(), setupEditMenu(), setupViewMenu(), setupToolsMenu());
    }

    public MenuBar build() {
        return menuBar;
    }

    private Menu setupFileMenu() {
        Menu fileMenu = new Menu(ResourceHandler.getString("Labels", "file"));

        MenuItem newFileItem = new MenuItem(ResourceHandler.getString("Buttons", "new"));
        newFileItem.setOnAction(e -> GeckoIOManager.getInstance().createNewProject());
        newFileItem.setAccelerator(Shortcuts.NEW.get());

        MenuItem openFileItem = new MenuItem(ResourceHandler.getString("Buttons", "open"));
        openFileItem.setOnAction(e -> GeckoIOManager.getInstance().loadGeckoProject());
        openFileItem.setAccelerator(Shortcuts.OPEN.get());

        MenuItem saveFileItem = new MenuItem(ResourceHandler.getString("Buttons", "save"));
        saveFileItem.setOnAction(e -> {
            File file = GeckoIOManager.getInstance().getFile();
            if (file != null) {
                GeckoIOManager.getInstance().saveGeckoProject(file);
            } else {
                File fileToSaveTo = GeckoIOManager.getInstance().getSaveFileChooser(FileTypes.JSON);
                if (fileToSaveTo != null) {
                    GeckoIOManager.getInstance().saveGeckoProject(fileToSaveTo);
                    GeckoIOManager.getInstance().setFile(fileToSaveTo);
                }
            }
        });
        saveFileItem.setAccelerator(Shortcuts.SAVE.get());

        MenuItem saveAsFileItem = new MenuItem(ResourceHandler.getString("Buttons", "save_as"));
        saveAsFileItem.setOnAction(e -> {
            File fileToSaveTo = GeckoIOManager.getInstance().getSaveFileChooser(FileTypes.JSON);
            if (fileToSaveTo != null) {
                GeckoIOManager.getInstance().saveGeckoProject(fileToSaveTo);
            }
        });
        saveAsFileItem.setAccelerator(Shortcuts.SAVE_AS.get());

        MenuItem importFileItem = new MenuItem(ResourceHandler.getString("Buttons", "import"));
        importFileItem.setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCombination.SHORTCUT_DOWN));
        importFileItem.setOnAction(e -> {
            File fileToImport = GeckoIOManager.getInstance().getOpenFileChooser(FileTypes.SYS);
            if (fileToImport != null) {
                GeckoIOManager.getInstance().importAutomatonFile(fileToImport);
            }
        });

        MenuItem exportFileItem = new MenuItem(ResourceHandler.getString("Buttons", "export"));
        exportFileItem.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN));
        exportFileItem.setOnAction(e -> {
            File fileToSaveTo = GeckoIOManager.getInstance().getSaveFileChooser(FileTypes.SYS);
            if (fileToSaveTo != null) {
                GeckoIOManager.getInstance().exportAutomatonFile(fileToSaveTo);
            }
        });

        fileMenu.getItems()
            .addAll(newFileItem, openFileItem, saveFileItem, saveAsFileItem, importFileItem, exportFileItem);
        return fileMenu;
    }

    private Menu setupEditMenu() {
        Menu editMenu = new Menu(ResourceHandler.getString("Labels", "edit"));

        // Edit history navigation:
        MenuItem undoMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "undo"));
        undoMenuItem.setOnAction(e -> actionManager.undo());
        undoMenuItem.setAccelerator(Shortcuts.UNDO.get());

        MenuItem redoMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "redo"));
        redoMenuItem.setOnAction(e -> actionManager.redo());
        redoMenuItem.setAccelerator(Shortcuts.REDO.get());

        SeparatorMenuItem historyToDataTransferSeparator = new SeparatorMenuItem();

        // Data transfer commands:
        MenuItem cutMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "cut"));
        cutMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createCutPositionableViewModelElementAction()));
        cutMenuItem.setAccelerator(Shortcuts.CUT.get());

        MenuItem copyMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "copy"));
        copyMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createCopyPositionableViewModelElementAction()));
        copyMenuItem.setAccelerator(Shortcuts.COPY.get());

        MenuItem pasteMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "paste"));
        pasteMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createPastePositionableViewModelElementAction()));
        pasteMenuItem.setAccelerator(Shortcuts.PASTE.get());

        // General selection commands:
        MenuItem selectAllMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "select_all"));
        selectAllMenuItem.setOnAction(e -> {
            Set<PositionableViewModelElement<?>> allElements = view.getAllDisplayedElements();
            actionManager.run(actionManager.getActionFactory().createSelectAction(allElements, true));
        });
        selectAllMenuItem.setAccelerator(Shortcuts.SELECT_ALL.get());

        MenuItem deselectAllMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "deselect_all"));
        deselectAllMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createDeselectAction()));
        deselectAllMenuItem.setAccelerator(Shortcuts.DESELECT_ALL.get());

        SeparatorMenuItem dataTransferToSelectionSeparator = new SeparatorMenuItem();

        SeparatorMenuItem renameRootSystemSeparator = new SeparatorMenuItem();

        CustomMenuItem renameRootSystemCustomMenuItem = getRenameRootSystemCustomMenuItem();

        editMenu.getItems()
            .addAll(undoMenuItem, redoMenuItem, historyToDataTransferSeparator, cutMenuItem, copyMenuItem,
                pasteMenuItem, dataTransferToSelectionSeparator, selectAllMenuItem, deselectAllMenuItem,
                renameRootSystemSeparator, renameRootSystemCustomMenuItem);

        return editMenu;
    }

    private CustomMenuItem getRenameRootSystemCustomMenuItem() {
        GeckoViewModel viewModel = view.getViewModel();
        TextField renameRootSystemTextField = new InspectorRenameField(actionManager,
            (Renamable) viewModel.getViewModelElement(viewModel.getGeckoModel().getRoot()));
        Label renameRootSystemLabel = new InspectorLabel(ResourceHandler.getString("Inspector", "rename_root_system"));
        VBox renameRootSystemContainer = new VBox(renameRootSystemLabel, renameRootSystemTextField);
        CustomMenuItem renameRootSystemCustomMenuItem = new CustomMenuItem(renameRootSystemContainer, false);
        renameRootSystemCustomMenuItem.setOnAction(e -> renameRootSystemTextField.requestFocus());
        return renameRootSystemCustomMenuItem;
    }

    private Menu setupViewMenu() {
        Menu viewMenu = new Menu(ResourceHandler.getString("Labels", "view"));

        // View change commands:
        MenuItem changeViewMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "change_view"));
        changeViewMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createViewSwitchAction(view.getCurrentView().getViewModel().getCurrentSystem(),
                !view.getCurrentView().getViewModel().isAutomatonEditor())));
        changeViewMenuItem.setAccelerator(Shortcuts.SWITCH_EDITOR.get());

        MenuItem goToParentSystemMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "go_to_parent_system"));
        goToParentSystemMenuItem.setOnAction(e -> {
            boolean isAutomatonEditor = view.getCurrentView().getViewModel().isAutomatonEditor();
            SystemViewModel parentSystem = view.getCurrentView().getViewModel().getParentSystem();
            actionManager.run(actionManager.getActionFactory().createViewSwitchAction(parentSystem, isAutomatonEditor));
        });
        goToParentSystemMenuItem.setAccelerator(Shortcuts.OPEN_PARENT_SYSTEM_EDITOR.get());

        MenuItem focusSelectedElementMenuItem =
            new MenuItem(ResourceHandler.getString("Buttons", "focus_selected_element"));
        focusSelectedElementMenuItem.setOnAction(e -> view.getCurrentView().getViewModel().moveToFocusedElement());
        focusSelectedElementMenuItem.setAccelerator(Shortcuts.FOCUS_SELECTED_ELEMENT.get());

        SeparatorMenuItem viewSwitchToZoomSeparator = new SeparatorMenuItem();

        // Zooming commands:

        MenuItem zoomInMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "zoom_in"));
        zoomInMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createZoomCenterAction(1.1)));
        zoomInMenuItem.setAccelerator(Shortcuts.ZOOM_IN.get());

        MenuItem zoomOutMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "zoom_out"));
        zoomOutMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createZoomCenterAction(1 / 1.1)));
        zoomOutMenuItem.setAccelerator(Shortcuts.ZOOM_OUT.get());

        SeparatorMenuItem zoomToAppearanceSeparator = new SeparatorMenuItem();

        MenuItem toggleAppearanceMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "toggle_appearance"));
        toggleAppearanceMenuItem.setOnAction(e -> view.toggleAppearance());
        toggleAppearanceMenuItem.setAccelerator(Shortcuts.TOGGLE_APPEARANCE.get());

        MenuItem searchElementsMenuItem = new MenuItem(ResourceHandler.getString("Buttons", "search_elements"));
        searchElementsMenuItem.setOnAction(e -> view.getCurrentView().toggleSearchWindow());
        searchElementsMenuItem.setAccelerator(Shortcuts.TOGGLE_SEARCH.get());

        viewMenu.getItems()
            .addAll(changeViewMenuItem, goToParentSystemMenuItem, focusSelectedElementMenuItem,
                viewSwitchToZoomSeparator, zoomInMenuItem, zoomOutMenuItem, zoomToAppearanceSeparator,
                toggleAppearanceMenuItem, searchElementsMenuItem);

        return viewMenu;
    }

    private Menu setupToolsMenu() {
        Menu toolsMenu = new Menu(ResourceHandler.getString("Labels", "tools"));

        // General tools:
        MenuItem cursorMenuItem = new MenuItem(ToolType.CURSOR.getLabel());
        cursorMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createSelectToolAction(ToolType.CURSOR)));
        cursorMenuItem.setAccelerator(Shortcuts.CURSOR_TOOL.get());

        MenuItem marqueeMenuItem = new MenuItem(ToolType.MARQUEE_TOOL.getLabel());
        marqueeMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createSelectToolAction(ToolType.MARQUEE_TOOL)));
        marqueeMenuItem.setAccelerator(Shortcuts.MARQUEE_TOOL.get());

        MenuItem panMenuItem = new MenuItem(ToolType.PAN.getLabel());
        panMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createSelectToolAction(ToolType.PAN)));
        panMenuItem.setAccelerator(Shortcuts.PAN_TOOL.get());

        SeparatorMenuItem generalFromSystemSeparator = new SeparatorMenuItem();

        // System view tools:
        MenuItem systemCreatorMenuItem = toolMenuItem(ToolType.SYSTEM_CREATOR, false);
        MenuItem systemConnectionCreatorMenuItem = toolMenuItem(ToolType.CONNECTION_CREATOR, false);
        MenuItem variableBlockCreatorMenuItem = toolMenuItem(ToolType.VARIABLE_BLOCK_CREATOR, false);

        SeparatorMenuItem systemFroAutomatonSeparator = new SeparatorMenuItem();

        // Automaton view tools:
        MenuItem stateCreatorMenuItem = toolMenuItem(ToolType.STATE_CREATOR, true);
        MenuItem edgeCreatorMenuItem = toolMenuItem(ToolType.EDGE_CREATOR, true);
        MenuItem regionCreatorMenuItem = toolMenuItem(ToolType.REGION_CREATOR, true);
        toolsMenu.getItems()
            .addAll(cursorMenuItem, marqueeMenuItem, panMenuItem, generalFromSystemSeparator, systemCreatorMenuItem,
                systemConnectionCreatorMenuItem, variableBlockCreatorMenuItem, systemFroAutomatonSeparator,
                stateCreatorMenuItem, edgeCreatorMenuItem, regionCreatorMenuItem);

        return toolsMenu;
    }

    private MenuItem toolMenuItem(ToolType toolType, boolean isAutomatonTool) {
        MenuItem toolMenuItem = new MenuItem(toolType.getLabel());
        toolMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createSelectToolAction(toolType)));
        toolMenuItem.disableProperty().bind(Bindings.createBooleanBinding(() -> {
            if (view.getCurrentView() == null) {
                return true;
            }
            return view.getCurrentView().getViewModel().isAutomatonEditor() != isAutomatonTool;
        }, view.getCurrentViewProperty()));
        return toolMenuItem;
    }
}
