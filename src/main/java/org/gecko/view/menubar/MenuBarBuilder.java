package org.gecko.view.menubar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import org.gecko.actions.ActionManager;
import org.gecko.application.GeckoIOManager;
import org.gecko.io.FileTypes;
import org.gecko.view.GeckoView;
import org.gecko.view.views.shortcuts.Shortcuts;
import org.gecko.viewmodel.PositionableViewModelElement;

public class MenuBarBuilder {

    private final MenuBar menuBar;
    private final GeckoView view;
    private final ActionManager actionManager;

    private static final String MATCHES_REGEX = "%d of %d matches";

    public MenuBarBuilder(GeckoView view, ActionManager actionManager) {
        this.view = view;
        this.actionManager = actionManager;
        menuBar = new MenuBar();

        // TODO
        menuBar.getMenus()
            .addAll(setupFileMenu(), new Menu("Edit"), new Menu("View"), new Menu("Tools"),
                new Menu("Help"), setupSearchBar());
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

    private Menu setupSearchBar() {
        Menu searchBarMenu = new Menu(null, createSearchBar());
        return searchBarMenu;
    }

    private ToolBar createSearchBar() {
        ToolBar searchBar = new ToolBar();

        // Close Search:
        Button closeButton = new Button("x");
        closeButton.setCancelButton(true);

        // Navigate Search:
        Button backwardButton = new Button("<");
        backwardButton.setDisable(true);

        Button forwardButton = new Button(">");
        forwardButton.setDisable(true);

        Label matchesLabel = new Label();
        matchesLabel.setTextFill(Color.BLACK);

        final List<PositionableViewModelElement<?>> matches = new ArrayList<>();
        TextField searchTextField = new TextField();
        searchTextField.setMaxHeight(1);
        searchTextField.setPromptText("Search");
        searchTextField.setOnAction(e -> {
            List<PositionableViewModelElement<?>> oldSearchMatches = new ArrayList<>(matches);
            oldSearchMatches.forEach(matches::remove);
            matches.addAll(view.getCurrentView().getViewModel().getElementsByName(searchTextField.getText()));

            if (!matches.isEmpty()) {
                actionManager.run(actionManager.getActionFactory()
                    .createFocusPositionableViewModelElementAction(matches.getFirst()));
                matchesLabel.setText(String.format(MATCHES_REGEX, 1, matches.size()));
                backwardButton.setDisable(true);
                forwardButton.setDisable(matches.size() == 1);
            } else {
                matchesLabel.setText(String.format(MATCHES_REGEX, 0, 0));
                backwardButton.setDisable(true);
                forwardButton.setDisable(true);
            }
        });

        backwardButton.setOnAction(e -> {
            if (!matches.isEmpty()) {
                int currentPosition = matches.indexOf(view.getCurrentView().getViewModel().getFocusedElement());
                actionManager.run(actionManager.getActionFactory()
                    .createFocusPositionableViewModelElementAction(matches.get(currentPosition - 1)));
                currentPosition--;
                matchesLabel.setText(String.format(MATCHES_REGEX, currentPosition + 1, matches.size()));
                backwardButton.setDisable(currentPosition == 0);
                forwardButton.setDisable(currentPosition == matches.size() - 1);
            }
        });

        forwardButton.setOnAction(e -> {
            if (!matches.isEmpty()) {
                int currentPosition = matches.indexOf(view.getCurrentView().getViewModel().getFocusedElement());
                actionManager.run(actionManager.getActionFactory()
                    .createFocusPositionableViewModelElementAction(matches.get(currentPosition + 1)));
                currentPosition++;
                matchesLabel.setText(String.format(MATCHES_REGEX, currentPosition + 1, matches.size()));
                backwardButton.setDisable(currentPosition == 0);
                forwardButton.setDisable(currentPosition == matches.size() - 1);
            }
        });

        closeButton.setOnAction(e -> {
            searchTextField.setText("");
            matchesLabel.setText("");
        });

        searchBar.getItems().addAll(closeButton, searchTextField, backwardButton, forwardButton, matchesLabel);
        return searchBar;
    }
}
