package org.gecko.view.menubar;

import java.io.File;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import org.gecko.actions.ActionManager;
import org.gecko.application.GeckoIOManager;
import org.gecko.view.GeckoView;

public class MenuBarBuilder {

    private final MenuBar menuBar;
    private final GeckoView view;
    private final ActionManager actionManager;

    public MenuBarBuilder(GeckoView view, ActionManager actionManager) {
        this.view = view;
        this.actionManager = actionManager;
        menuBar = new MenuBar();

        // TODO
        menuBar.getMenus().addAll(setupFileMenu(), new Menu("Edit"), new Menu("View"), new Menu("Tools"), new Menu("Help"));
    }

    public MenuBar build() {
        return menuBar;
    }

    private Menu setupFileMenu() {
        Menu fileMenu = new Menu("File");

        MenuItem newFileItem = new MenuItem("New");

        MenuItem openFileItem = new MenuItem("Open");
        openFileItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json Files", "*.json"));
            File selectedFile = fileChooser.showOpenDialog(GeckoIOManager.getInstance().getStage());
            GeckoIOManager.getInstance().loadGeckoProject(selectedFile);
        });

        MenuItem saveFileItem = new MenuItem("Save");
        saveFileItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json Files", "*.json"));
            File selectedFile = fileChooser.showSaveDialog(GeckoIOManager.getInstance().getStage());
            GeckoIOManager.getInstance().saveGeckoProject(selectedFile);
        });

        MenuItem importFileItem = new MenuItem("Import");

        MenuItem exportFileItem = new MenuItem("Export");

        fileMenu.getItems().addAll(newFileItem, openFileItem, saveFileItem, importFileItem, exportFileItem);
        return fileMenu;
    }
}
