package org.gecko.view.menubar;

import java.io.File;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import org.gecko.actions.ActionManager;
import org.gecko.application.GeckoIOManager;
import org.gecko.io.FileTypes;
import org.gecko.tools.CursorTool;
import org.gecko.tools.EdgeCreatorTool;
import org.gecko.tools.MarqueeTool;
import org.gecko.tools.PanTool;
import org.gecko.tools.RegionCreatorTool;
import org.gecko.tools.StateCreatorTool;
import org.gecko.tools.SystemConnectionCreatorTool;
import org.gecko.tools.SystemCreatorTool;
import org.gecko.tools.VariableBlockCreatorTool;
import org.gecko.tools.ZoomTool;
import org.gecko.view.GeckoView;
import org.gecko.view.views.shortcuts.Shortcuts;

public class MenuBarBuilder {
    private final MenuBar menuBar;
    private final GeckoView view;
    private final ActionManager actionManager;

    public MenuBarBuilder(GeckoView view, ActionManager actionManager) {
        this.view = view;
        this.actionManager = actionManager;
        menuBar = new MenuBar();


        // TODO
        menuBar.getMenus().addAll(setupFileMenu(), new Menu("Edit"), new Menu("View"), setupToolsMenu(),
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

    private Menu setupToolsMenu() {
        Menu toolsMenu = new Menu("Tools");

        // General tools:
        MenuItem cursorMenuItem = new MenuItem("Cursor Tool");
        cursorMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
                .createSelectToolAction(view.getCurrentView(), new CursorTool(actionManager,
                 view.getCurrentView().getViewModel().getSelectionManager(), view.getCurrentView().getViewModel()))));

        MenuItem marqueeMenuItem = new MenuItem("Marquee Tool");
        marqueeMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectToolAction(view.getCurrentView(), new MarqueeTool(actionManager))));

        MenuItem panMenuItem = new MenuItem("Pan Tool");
        panMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectToolAction(view.getCurrentView(), new PanTool(actionManager))));

        MenuItem zoomMenuItem = new MenuItem("Zoom Tool");
        zoomMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectToolAction(view.getCurrentView(), new ZoomTool(actionManager))));

        // System view tools:
        MenuItem systemCreatorMenuItem = new MenuItem("System Creator Tool");
        systemCreatorMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectToolAction(view.getCurrentView(), new SystemCreatorTool(actionManager))));

        MenuItem systemConnectionCreatorMenuItem = new MenuItem("System Connection Creator Tool");
        systemConnectionCreatorMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectToolAction(view.getCurrentView(), new SystemConnectionCreatorTool(actionManager))));

        MenuItem variableBlockCreatorMenuItem = new MenuItem("Variable Block Creator Tool");
        variableBlockCreatorMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectToolAction(view.getCurrentView(), new VariableBlockCreatorTool(actionManager))));

        // Automaton view tools:
        MenuItem stateCreatorMenuItem = new MenuItem("State Creator Tool");
        stateCreatorMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectToolAction(view.getCurrentView(), new StateCreatorTool(actionManager))));
        stateCreatorMenuItem.setDisable(true);

        MenuItem edgeCreatorMenuItem = new MenuItem("Edge Creator Tool");
        edgeCreatorMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectToolAction(view.getCurrentView(), new EdgeCreatorTool(actionManager))));
        edgeCreatorMenuItem.setDisable(true);

        MenuItem regionCreatorMenuItem = new MenuItem("Region Creator Tool");
        regionCreatorMenuItem.setOnAction(e -> actionManager.run(actionManager.getActionFactory()
            .createSelectToolAction(view.getCurrentView(), new RegionCreatorTool(actionManager))));
        regionCreatorMenuItem.setDisable(true);

        toolsMenu.getItems().addAll(cursorMenuItem, marqueeMenuItem, panMenuItem, zoomMenuItem, systemCreatorMenuItem,
            systemConnectionCreatorMenuItem, variableBlockCreatorMenuItem, stateCreatorMenuItem, edgeCreatorMenuItem,
            regionCreatorMenuItem);

        return toolsMenu;
    }

    public static void updateToolsMenu(Menu toolsMenu, boolean isAutomatonEditor) {
        if (isAutomatonEditor) {
            toolsMenu.getItems().get(4).setDisable(true);
            toolsMenu.getItems().get(5).setDisable(true);
            toolsMenu.getItems().get(6).setDisable(true);
            toolsMenu.getItems().get(7).setDisable(false);
            toolsMenu.getItems().get(8).setDisable(false);
            toolsMenu.getItems().get(9).setDisable(false);
        } else {
            toolsMenu.getItems().get(4).setDisable(false);
            toolsMenu.getItems().get(5).setDisable(false);
            toolsMenu.getItems().get(6).setDisable(false);
            toolsMenu.getItems().get(7).setDisable(true);
            toolsMenu.getItems().get(8).setDisable(true);
            toolsMenu.getItems().get(9).setDisable(true);
        }
    }
}
