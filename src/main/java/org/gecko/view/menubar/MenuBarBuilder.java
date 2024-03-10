package org.gecko.view.menubar;

import java.io.File;
import java.util.List;
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
import org.gecko.tools.Tool;
import org.gecko.tools.ToolType;
import org.gecko.view.GeckoView;
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

    private static final String FILE_MENU = "File";
    private static final String EDIT_MENU = "Edit";
    private static final String VIEW_MENU = "View";
    private static final String TOOLS_MENU = "Tools";
    private static final String NEW_MENU_ITEM = "New";
    private static final String OPEN_MENU_ITEM = "Open";
    private static final String SAVE_MENU_ITEM = "Save";
    private static final String SAVE_AS_MENU_ITEM = "Save As";
    private static final String IMPORT_MENU_ITEM = "Import";
    private static final String EXPORT_MENU_ITEM = "Export";
    private static final String UNDO_MENU_ITEM = "Undo";
    private static final String REDO_MENU_ITEM = "Redo";
    private static final String CUT_MENU_ITEM = "Cut";
    private static final String COPY_MENU_ITEM = "Copy";
    private static final String PASTE_MENU_ITEM = "Paste";
    private static final String SELECT_ALL_MENU_ITEM = "Select All";
    private static final String DESELECT_ALL_MENU_ITEM = "Deselect All";
    private static final String RENAME_ROOT__MENU_ITEM = "Rename Root System";
    private static final String CHANGE_VIEW_MENU_ITEM = "Change View";
    private static final String GO_TO_PARENT_MENU_ITEM = "Go To Parent System";
    private static final String FOCUS_MENU_ITEM = "Focus Selected Element";
    private static final String ZOOM_IN_MENU_ITEM = "Zoom In";
    private static final String ZOOM_OUT_MENU_ITEM = "Zoom Out";
    private static final String TOGGLE_MENU_ITEM = "Toggle Appearance";
    private static final String SEARCH_MENU_ITEM = "Search Elements";
    private static final double ZOOM_SCALE = 1.1;

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
        Menu fileMenu = new Menu(FILE_MENU);

        MenuItem newFileItem = new MenuItem(NEW_MENU_ITEM);
        newFileItem.setOnAction(e -> GeckoIOManager.getInstance().createNewProject());
        newFileItem.setAccelerator(Shortcuts.NEW.get());

        MenuItem openFileItem = new MenuItem(OPEN_MENU_ITEM);
        openFileItem.setOnAction(e -> GeckoIOManager.getInstance().loadGeckoProject());
        openFileItem.setAccelerator(Shortcuts.OPEN.get());

        MenuItem saveFileItem = new MenuItem(SAVE_MENU_ITEM);
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

        MenuItem saveAsFileItem = new MenuItem(SAVE_AS_MENU_ITEM);
        saveAsFileItem.setOnAction(e -> {
            File fileToSaveTo = GeckoIOManager.getInstance().getSaveFileChooser(FileTypes.JSON);
            if (fileToSaveTo != null) {
                GeckoIOManager.getInstance().saveGeckoProject(fileToSaveTo);
            }
        });
        saveAsFileItem.setAccelerator(Shortcuts.SAVE_AS.get());

        MenuItem importFileItem = new MenuItem(IMPORT_MENU_ITEM);
        importFileItem.setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCombination.SHORTCUT_DOWN));
        importFileItem.setOnAction(e -> {
            File fileToImport = GeckoIOManager.getInstance().getOpenFileChooser(FileTypes.SYS);
            if (fileToImport != null) {
                GeckoIOManager.getInstance().importAutomatonFile(fileToImport);
            }
        });

        MenuItem exportFileItem = new MenuItem(EXPORT_MENU_ITEM);
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
        Menu editMenu = new Menu(EDIT_MENU);

        // Edit history navigation:
        MenuItem undoMenuItem = new MenuItem(UNDO_MENU_ITEM);
        undoMenuItem.setOnAction(e -> actionManager.undo());
        undoMenuItem.setAccelerator(Shortcuts.UNDO.get());

        MenuItem redoMenuItem = new MenuItem(REDO_MENU_ITEM);
        redoMenuItem.setOnAction(e -> actionManager.redo());
        redoMenuItem.setAccelerator(Shortcuts.REDO.get());

        SeparatorMenuItem historyToDataTransferSeparator = new SeparatorMenuItem();

        // Data transfer commands:
        MenuItem cutMenuItem = new MenuItem(CUT_MENU_ITEM);
        cutMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createCutPositionableViewModelElementAction()));
        cutMenuItem.setAccelerator(Shortcuts.CUT.get());

        MenuItem copyMenuItem = new MenuItem(COPY_MENU_ITEM);
        copyMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createCopyPositionableViewModelElementAction()));
        copyMenuItem.setAccelerator(Shortcuts.COPY.get());

        MenuItem pasteMenuItem = new MenuItem(PASTE_MENU_ITEM);
        pasteMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createPastePositionableViewModelElementAction()));
        pasteMenuItem.setAccelerator(Shortcuts.PASTE.get());

        // General selection commands:
        MenuItem selectAllMenuItem = new MenuItem(SELECT_ALL_MENU_ITEM);
        selectAllMenuItem.setOnAction(e -> {
            Set<PositionableViewModelElement<?>> allElements = view.getAllDisplayedElements();
            actionManager.run(actionManager.getActionFactory().createSelectAction(allElements, true));
        });
        selectAllMenuItem.setAccelerator(Shortcuts.SELECT_ALL.get());

        MenuItem deselectAllMenuItem = new MenuItem(DESELECT_ALL_MENU_ITEM);
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
        Label renameRootSystemLabel = new InspectorLabel(RENAME_ROOT__MENU_ITEM);
        VBox renameRootSystemContainer = new VBox(renameRootSystemLabel, renameRootSystemTextField);
        CustomMenuItem renameRootSystemCustomMenuItem = new CustomMenuItem(renameRootSystemContainer, false);
        renameRootSystemCustomMenuItem.setOnAction(e -> renameRootSystemTextField.requestFocus());
        return renameRootSystemCustomMenuItem;
    }

    private Menu setupViewMenu() {
        Menu viewMenu = new Menu(VIEW_MENU);

        // View change commands:
        MenuItem changeViewMenuItem = new MenuItem(CHANGE_VIEW_MENU_ITEM);
        changeViewMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createViewSwitchAction(view.getCurrentView().getViewModel().getCurrentSystem(),
                !view.getCurrentView().getViewModel().isAutomatonEditor())));
        changeViewMenuItem.setAccelerator(Shortcuts.SWITCH_EDITOR.get());

        MenuItem goToParentSystemMenuItem = new MenuItem(GO_TO_PARENT_MENU_ITEM);
        goToParentSystemMenuItem.setOnAction(e -> {
            boolean isAutomatonEditor = view.getCurrentView().getViewModel().isAutomatonEditor();
            SystemViewModel parentSystem = view.getCurrentView().getViewModel().getParentSystem();
            actionManager.run(actionManager.getActionFactory().createViewSwitchAction(parentSystem, isAutomatonEditor));
        });
        goToParentSystemMenuItem.setAccelerator(Shortcuts.OPEN_PARENT_SYSTEM_EDITOR.get());

        MenuItem focusSelectedElementMenuItem = new MenuItem(FOCUS_MENU_ITEM);
        focusSelectedElementMenuItem.setOnAction(e -> view.getCurrentView().getViewModel().moveToFocusedElement());
        focusSelectedElementMenuItem.setAccelerator(Shortcuts.FOCUS_SELECTED_ELEMENT.get());

        SeparatorMenuItem viewSwitchToZoomSeparator = new SeparatorMenuItem();

        // Zooming commands:

        MenuItem zoomInMenuItem = new MenuItem(ZOOM_IN_MENU_ITEM);
        zoomInMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createZoomCenterAction(ZOOM_SCALE)));
        zoomInMenuItem.setAccelerator(Shortcuts.ZOOM_IN.get());

        MenuItem zoomOutMenuItem = new MenuItem(ZOOM_OUT_MENU_ITEM);
        zoomOutMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createZoomCenterAction(1 / ZOOM_SCALE)));
        zoomOutMenuItem.setAccelerator(Shortcuts.ZOOM_OUT.get());

        SeparatorMenuItem zoomToAppearanceSeparator = new SeparatorMenuItem();

        MenuItem toggleAppearanceMenuItem = new MenuItem(TOGGLE_MENU_ITEM);
        toggleAppearanceMenuItem.setOnAction(e -> view.toggleAppearance());
        toggleAppearanceMenuItem.setAccelerator(Shortcuts.TOGGLE_APPEARANCE.get());

        MenuItem searchElementsMenuItem = new MenuItem(SEARCH_MENU_ITEM);
        searchElementsMenuItem.setOnAction(e -> view.getCurrentView().toggleSearchWindow());
        searchElementsMenuItem.setAccelerator(Shortcuts.TOGGLE_SEARCH.get());

        viewMenu.getItems()
            .addAll(changeViewMenuItem, goToParentSystemMenuItem, focusSelectedElementMenuItem,
                viewSwitchToZoomSeparator, zoomInMenuItem, zoomOutMenuItem, zoomToAppearanceSeparator,
                toggleAppearanceMenuItem, searchElementsMenuItem);

        return viewMenu;
    }

    private Menu setupToolsMenu() {
        Menu toolsMenu = new Menu(TOOLS_MENU);

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

    /**
     * Updates the tools menu with the updated tool lists.
     *
     * @param menuBar   The menu bar to update
     * @param toolLists The updated tool lists
     */
    public static void updateToolsMenu(MenuBar menuBar, List<List<Tool>> toolLists) {
        List<Tool> constantTools = toolLists.get(0);
        List<Tool> variableTools = toolLists.get(1);

        menuBar.getMenus()
            .stream()
            .filter(menu -> menu.getText().equals(TOOLS_MENU))
            .findFirst()
            .ifPresent(toolsMenu -> toolsMenu.getItems().forEach(toolMenu -> {
                Tool constantTool = constantTools.stream()
                    .filter(tool -> tool.getToolType().getLabel().equals(toolMenu.getText()))
                    .findAny()
                    .orElse(null);
                Tool activeTool = variableTools.stream()
                    .filter(tool -> tool.getToolType().getLabel().equals(toolMenu.getText()))
                    .findAny()
                    .orElse(null);
                if (constantTool == null) {
                    toolMenu.setDisable(activeTool == null);
                }
            }));
    }
}
