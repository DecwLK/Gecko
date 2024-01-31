package org.gecko.view.menubar;

import java.io.File;
import java.util.Set;
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


        // TODO
        menuBar.getMenus()
            .addAll(setupFileMenu(), setupEditMenu(), new Menu("View"), setupToolsMenu(),
                new Menu("Help"));
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

        MenuItem exportFileItem = new MenuItem("Export");
        exportFileItem.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN));

        fileMenu.getItems()
            .addAll(newFileItem, openFileItem, saveFileItem, saveAsFileItem, importFileItem, exportFileItem);
        return fileMenu;
    }

    private  Menu setupEditMenu() {
        Menu editMenu = new Menu("Edit");

        // Edit history navigation:
        MenuItem undoMenuItem = new MenuItem("Undo");
        MenuItem redoMenuItem = new MenuItem("Redo");

        SeparatorMenuItem historyToDataTransferSeparator = new SeparatorMenuItem();

        // Data transfer commands:
        MenuItem cutMenuItem = new MenuItem("Cut");
        MenuItem copyMenuItem = new MenuItem("Copy");
        MenuItem pasteMenuItem = new MenuItem("Paste");

        // General selection commands:
        MenuItem selectAllMenuItem = new MenuItem("Select All");
        selectAllMenuItem.setOnAction(e -> {
            Set<PositionableViewModelElement<?>> allElements = view.getAllDisplayedElements();
            actionManager.run(actionManager.getActionFactory().createSelectAction(allElements, true));
        });

        MenuItem deselectAllMenuItem = new MenuItem("Deselect All");
        deselectAllMenuItem.setOnAction(e
            -> actionManager.run(actionManager.getActionFactory().createDeselectAction()));

        SeparatorMenuItem dataTransferToSelectionSeparator = new SeparatorMenuItem();

        editMenu.getItems().addAll(undoMenuItem, redoMenuItem, historyToDataTransferSeparator, cutMenuItem,
            copyMenuItem, pasteMenuItem, dataTransferToSelectionSeparator, selectAllMenuItem, deselectAllMenuItem);

        return editMenu;
    }

    private Menu setupToolsMenu() {
        Menu toolsMenu = new Menu("Tools");

        Menu changeToolMenu = new Menu("Change Tool");

        // General tools:
        MenuItem cursorMenuItem = new MenuItem("Cursor Tool");
        cursorMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
                .createSelectToolAction(ToolType.CURSOR)));

        MenuItem marqueeMenuItem = new MenuItem("Marquee Tool");
        marqueeMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectToolAction(ToolType.MARQUEE_TOOL)));

        MenuItem panMenuItem = new MenuItem("Pan Tool");
        panMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectToolAction(ToolType.PAN)));

        MenuItem zoomMenuItem = new MenuItem("Zoom Tool");
        zoomMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectToolAction(ToolType.ZOOM_TOOL)));

        SeparatorMenuItem generalFromSystemSeparator = new SeparatorMenuItem();

        // System view tools:
        MenuItem systemCreatorMenuItem = new MenuItem("System Creator Tool");
        systemCreatorMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectToolAction(ToolType.SYSTEM_CREATOR)));

        MenuItem systemConnectionCreatorMenuItem = new MenuItem("System Connection Creator Tool");
        systemConnectionCreatorMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectToolAction(ToolType.CONNECTION_CREATOR)));

        MenuItem variableBlockCreatorMenuItem = new MenuItem("Variable Block Creator Tool");
        variableBlockCreatorMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectToolAction(ToolType.VARIABLE_BLOCK_CREATOR)));

        SeparatorMenuItem systemFroAutomatonSeparator = new SeparatorMenuItem();

        // Automaton view tools:
        MenuItem stateCreatorMenuItem = new MenuItem("State Creator Tool");
        stateCreatorMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectToolAction(ToolType.STATE_CREATOR)));
        stateCreatorMenuItem.setDisable(true);

        MenuItem edgeCreatorMenuItem = new MenuItem("Edge Creator Tool");
        edgeCreatorMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectToolAction(ToolType.EDGE_CREATOR)));
        edgeCreatorMenuItem.setDisable(true);

        MenuItem regionCreatorMenuItem = new MenuItem("Region Creator Tool");
        regionCreatorMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectToolAction(ToolType.REGION_CREATOR)));
        regionCreatorMenuItem.setDisable(true);

        changeToolMenu.getItems().addAll(cursorMenuItem, marqueeMenuItem, panMenuItem, zoomMenuItem,
            generalFromSystemSeparator, systemCreatorMenuItem, systemConnectionCreatorMenuItem,
            variableBlockCreatorMenuItem, systemFroAutomatonSeparator, stateCreatorMenuItem, edgeCreatorMenuItem,
            regionCreatorMenuItem);

        toolsMenu.getItems().add(changeToolMenu);

        return toolsMenu;
    }

    public static void updateToolsMenu(Menu toolsMenu, boolean isAutomatonEditor) {
        Menu changeToolMenu = (Menu) toolsMenu.getItems().getFirst();
        if (isAutomatonEditor) {
            changeToolMenu.getItems().get(5).setDisable(true);
            changeToolMenu.getItems().get(6).setDisable(true);
            changeToolMenu.getItems().get(7).setDisable(true);
            changeToolMenu.getItems().get(9).setDisable(false);
            changeToolMenu.getItems().get(10).setDisable(false);
            changeToolMenu.getItems().get(11).setDisable(false);
        } else {
            changeToolMenu.getItems().get(5).setDisable(false);
            changeToolMenu.getItems().get(6).setDisable(false);
            changeToolMenu.getItems().get(7).setDisable(false);
            changeToolMenu.getItems().get(9).setDisable(true);
            changeToolMenu.getItems().get(10).setDisable(true);
            changeToolMenu.getItems().get(11).setDisable(true);
        }
    }
}
