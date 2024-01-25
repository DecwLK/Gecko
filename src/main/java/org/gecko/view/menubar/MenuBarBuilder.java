package org.gecko.view.menubar;

import java.io.File;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import org.gecko.actions.ActionManager;
import org.gecko.application.GeckoIOManager;
import org.gecko.io.FileTypes;
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
        menuBar.getMenus()
            .addAll(setupFileMenu(), new Menu("Edit"), new Menu("View"), new Menu("Tools"), new Menu("Help"));
    }

    public MenuBar build() {
        return menuBar;
    }

    private Menu setupFileMenu() {
        Menu fileMenu = new Menu("File");

        MenuItem newFileItem = new MenuItem("New");
        newFileItem.setOnAction(e -> GeckoIOManager.getInstance().createNewProject());

        MenuItem openFileItem = new MenuItem("Open");
        openFileItem.setOnAction(e -> GeckoIOManager.getInstance().loadGeckoProject());

        MenuItem saveFileItem = new MenuItem("Save");
        saveFileItem.setOnAction(e -> {
            File file = GeckoIOManager.getFile();
            if (file != null) {
                GeckoIOManager.getInstance().saveGeckoProject(file);
            } else {
                saveToFile();
            }
        });

        MenuItem saveAsFileItem = new MenuItem("Save As");
        saveAsFileItem.setOnAction(e -> saveToFile());

        MenuItem importFileItem = new MenuItem("Import");

        MenuItem exportFileItem = new MenuItem("Export");

        fileMenu.getItems()
            .addAll(newFileItem, openFileItem, saveFileItem, saveAsFileItem, importFileItem, exportFileItem);
        return fileMenu;
    }

    private void saveToFile() {
        File fileToSaveTo = GeckoIOManager.getInstance().saveFileChooser(FileTypes.JSON);
        if (fileToSaveTo != null) {
            GeckoIOManager.getInstance().saveGeckoProject(fileToSaveTo);
        }
    }
}
