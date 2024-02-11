package org.gecko.view.menubar;

import java.io.File;
import java.util.List;
import java.util.Set;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import org.gecko.actions.ActionManager;
import org.gecko.application.GeckoIOManager;
import org.gecko.io.FileTypes;
import org.gecko.tools.Tool;
import org.gecko.tools.ToolType;
import org.gecko.view.GeckoView;
import org.gecko.view.views.shortcuts.Shortcuts;
import org.gecko.viewmodel.PositionableViewModelElement;

public class MenuBarBuilder {
    private final MenuBar menuBar;
    private final GeckoView view;
    private final ActionManager actionManager;

    public MenuBarBuilder(GeckoView view, ActionManager actionManager) {
        this.view = view;
        this.actionManager = actionManager;
        menuBar = new MenuBar();

        menuBar.getMenus().addAll(setupFileMenu(), setupEditMenu(), setupViewMenu(), setupToolsMenu(), setupHelpMenu());
    }

    public MenuBar build() {
        return menuBar;
    }

    private Menu setupFileMenu() {
        Menu fileMenu = new Menu("File");

        MenuItem newFileItem = new MenuItem("New");
        newFileItem.setOnAction(e -> GeckoIOManager.getInstance().createNewProject());
        newFileItem.setAccelerator(Shortcuts.NEW.get());

        MenuItem openFileItem = new MenuItem("Open");
        openFileItem.setOnAction(e -> GeckoIOManager.getInstance().loadGeckoProject());
        openFileItem.setAccelerator(Shortcuts.OPEN.get());

        MenuItem saveFileItem = new MenuItem("Save");
        saveFileItem.setOnAction(e -> {
            File file = GeckoIOManager.getInstance().getFile();
            if (file != null) {
                GeckoIOManager.getInstance().saveGeckoProject(file);
            } else {
                File fileToSaveTo = GeckoIOManager.getInstance().saveFileChooser(FileTypes.JSON);
                if (fileToSaveTo != null) {
                    GeckoIOManager.getInstance().saveGeckoProject(fileToSaveTo);
                    GeckoIOManager.getInstance().setFile(fileToSaveTo);
                }
            }
        });
        saveFileItem.setAccelerator(Shortcuts.SAVE.get());

        MenuItem saveAsFileItem = new MenuItem("Save As");
        saveAsFileItem.setOnAction(e -> {
            File fileToSaveTo = GeckoIOManager.getInstance().saveFileChooser(FileTypes.JSON);
            if (fileToSaveTo != null) {
                GeckoIOManager.getInstance().saveGeckoProject(fileToSaveTo);
            }
        });
        saveAsFileItem.setAccelerator(Shortcuts.SAVE_AS.get());

        MenuItem importFileItem = new MenuItem("Import");
        importFileItem.setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCombination.SHORTCUT_DOWN));
        importFileItem.setOnAction(e -> {
            File fileToImport = GeckoIOManager.getInstance().openFileChooser(FileTypes.SYS);
            if (fileToImport != null) {
                GeckoIOManager.getInstance().importAutomatonFile(fileToImport);
            }
        });

        MenuItem exportFileItem = new MenuItem("Export");
        exportFileItem.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN));
        exportFileItem.setOnAction(e -> {
            File fileToSaveTo = GeckoIOManager.getInstance().saveFileChooser(FileTypes.SYS);
            if (fileToSaveTo != null) {
                GeckoIOManager.getInstance().exportAutomatonFile(fileToSaveTo);
            }
        });

        fileMenu.getItems()
            .addAll(newFileItem, openFileItem, saveFileItem, saveAsFileItem, importFileItem, exportFileItem);
        return fileMenu;
    }

    private Menu setupEditMenu() {
        Menu editMenu = new Menu("Edit");

        // Edit history navigation:
        MenuItem undoMenuItem = new MenuItem("Undo");
        undoMenuItem.setOnAction(e -> actionManager.undo());
        undoMenuItem.setAccelerator(Shortcuts.UNDO.get());

        MenuItem redoMenuItem = new MenuItem("Redo");
        redoMenuItem.setOnAction(e -> actionManager.redo());
        redoMenuItem.setAccelerator(Shortcuts.REDO.get());

        SeparatorMenuItem historyToDataTransferSeparator = new SeparatorMenuItem();

        // Data transfer commands:
        MenuItem cutMenuItem = new MenuItem("Cut");
        cutMenuItem.setOnAction(e -> actionManager.cut());
        cutMenuItem.setAccelerator(Shortcuts.CUT.get());

        MenuItem copyMenuItem = new MenuItem("Copy");
        copyMenuItem.setOnAction(e -> actionManager.copy());
        copyMenuItem.setAccelerator(Shortcuts.COPY.get());

        MenuItem pasteMenuItem = new MenuItem("Paste");
        pasteMenuItem.setOnAction(e -> actionManager.paste());
        pasteMenuItem.setAccelerator(Shortcuts.PASTE.get());

        // General selection commands:
        MenuItem selectAllMenuItem = new MenuItem("Select All");
        selectAllMenuItem.setOnAction(e -> {
            Set<PositionableViewModelElement<?>> allElements = view.getAllDisplayedElements();
            actionManager.run(actionManager.getActionFactory().createSelectAction(allElements, true));
        });
        selectAllMenuItem.setAccelerator(Shortcuts.SELECT_ALL.get());

        MenuItem deselectAllMenuItem = new MenuItem("Deselect All");
        deselectAllMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createDeselectAction()));
        deselectAllMenuItem.setAccelerator(Shortcuts.DESELECT_ALL.get());

        SeparatorMenuItem dataTransferToSelectionSeparator = new SeparatorMenuItem();

        editMenu.getItems()
            .addAll(undoMenuItem, redoMenuItem, historyToDataTransferSeparator, cutMenuItem, copyMenuItem,
                pasteMenuItem, dataTransferToSelectionSeparator, selectAllMenuItem, deselectAllMenuItem);

        return editMenu;
    }

    private Menu setupViewMenu() {
        Menu viewMenu = new Menu("View");

        // View change commands:
        MenuItem changeViewMenuItem = new MenuItem("Change View");
        changeViewMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createViewSwitchAction(view.getCurrentView().getViewModel().getCurrentSystem(),
                !view.getCurrentView().getViewModel().isAutomatonEditor())));
        changeViewMenuItem.setAccelerator(Shortcuts.SWITCH_EDITOR.get());

        MenuItem goToParentSystemMenuItem = new MenuItem("Go To Parent System");
        goToParentSystemMenuItem.setOnAction(e -> {
            if (view.getCurrentView().getViewModel().getCurrentSystem().getName().equals("root")) {
                Alert alert =
                    new Alert(Alert.AlertType.INFORMATION, "The root system does not have a parent.", ButtonType.OK);
                alert.showAndWait();
            } else {
                if (view.getCurrentView().getViewModel().isAutomatonEditor()) {
                    changeViewMenuItem.fire();
                }
                actionManager.run(actionManager.getActionFactory()
                    .createViewSwitchAction(view.getCurrentView().getViewModel().getParentSystem(), false));
            }
        });
        goToParentSystemMenuItem.setAccelerator(Shortcuts.OPEN_PARENT_SYSTEM_EDITOR.get());

        SeparatorMenuItem viewSwitchToZoomSeparator = new SeparatorMenuItem();

        // Zooming commands:

        MenuItem zoomInMenuItem = new MenuItem("Zoom In");
        zoomInMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createZoomCenterAction(1.1)));
        zoomInMenuItem.setAccelerator(Shortcuts.ZOOM_IN.get());

        MenuItem zoomOutMenuItem = new MenuItem("Zoom Out");
        zoomOutMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createZoomCenterAction(1 / 1.1)));
        zoomOutMenuItem.setAccelerator(Shortcuts.ZOOM_OUT.get());

        viewMenu.getItems()
            .addAll(changeViewMenuItem, goToParentSystemMenuItem, viewSwitchToZoomSeparator, zoomInMenuItem,
                zoomOutMenuItem);

        return viewMenu;
    }

    private Menu setupToolsMenu() {
        Menu toolsMenu = new Menu("Tools");

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
        MenuItem systemCreatorMenuItem = new MenuItem(ToolType.SYSTEM_CREATOR.getLabel());
        systemCreatorMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createSelectToolAction(ToolType.SYSTEM_CREATOR)));

        MenuItem systemConnectionCreatorMenuItem = new MenuItem(ToolType.CONNECTION_CREATOR.getLabel());
        systemConnectionCreatorMenuItem.setOnAction(e -> actionManager.run(
            actionManager.getActionFactory().createSelectToolAction(ToolType.CONNECTION_CREATOR)));

        MenuItem variableBlockCreatorMenuItem = new MenuItem(ToolType.VARIABLE_BLOCK_CREATOR.getLabel());
        variableBlockCreatorMenuItem.setOnAction(e -> actionManager.run(
            actionManager.getActionFactory().createSelectToolAction(ToolType.VARIABLE_BLOCK_CREATOR)));

        SeparatorMenuItem systemFroAutomatonSeparator = new SeparatorMenuItem();

        // Automaton view tools:
        MenuItem stateCreatorMenuItem = new MenuItem(ToolType.STATE_CREATOR.getLabel());
        stateCreatorMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createSelectToolAction(ToolType.STATE_CREATOR)));
        stateCreatorMenuItem.setDisable(true);

        MenuItem edgeCreatorMenuItem = new MenuItem(ToolType.EDGE_CREATOR.getLabel());
        edgeCreatorMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createSelectToolAction(ToolType.EDGE_CREATOR)));
        edgeCreatorMenuItem.setDisable(true);

        MenuItem regionCreatorMenuItem = new MenuItem(ToolType.REGION_CREATOR.getLabel());
        regionCreatorMenuItem.setOnAction(
            e -> actionManager.run(actionManager.getActionFactory().createSelectToolAction(ToolType.REGION_CREATOR)));
        regionCreatorMenuItem.setDisable(true);

        toolsMenu.getItems()
            .addAll(cursorMenuItem, marqueeMenuItem, panMenuItem, generalFromSystemSeparator, systemCreatorMenuItem,
                systemConnectionCreatorMenuItem, variableBlockCreatorMenuItem, systemFroAutomatonSeparator,
                stateCreatorMenuItem, edgeCreatorMenuItem, regionCreatorMenuItem);

        return toolsMenu;
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
            .filter(menu -> menu.getText().equals("Tools"))
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

    private Menu setupHelpMenu() {
        Menu helpMenu = new Menu("Help");

        MenuItem findElementsMenuItem = new MenuItem("Find Elements");
        findElementsMenuItem.setOnAction(e -> {
            view.getCurrentView().activateSearchWindow(true);
        });

        helpMenu.getItems().add(findElementsMenuItem);
        return helpMenu;
    }
}
