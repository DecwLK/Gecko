package org.gecko.view.views;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.views.shortcuts.Shortcuts;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

/**
 * Represents a builder for floating UI elements in the view, like different kinds of {@link Button}s and
 * {@link Label}s.
 */
public class FloatingUIBuilder {

    private static final int DEFAULT_BUTTON_SIZE = 30;
    private static final String FLOATING_BUTTON_STYLE_CLASS = "floating-ui-button";
    private static final String ZOOM_IN_STYLE_CLASS = "floating-zoom-in-button";
    private static final String ZOOM_OUT_STYLE_CLASS = "floating-zoom-out-button";
    private static final String CLOSE_BUTTON = "x";
    private static final String LEFT_BUTTON = "<";
    private static final String RIGHT_BUTTON = ">";

    private final ActionManager actionManager;
    private final EditorViewModel editorViewModel;

    public FloatingUIBuilder(ActionManager actionManager, EditorViewModel editorViewModel) {
        this.actionManager = actionManager;
        this.editorViewModel = editorViewModel;
    }

    public Node buildZoomButtons() {
        VBox zoomButtons = new VBox();

        Button zoomInButton = createStyledButton();
        zoomInButton.getStyleClass().add(ZOOM_IN_STYLE_CLASS);
        zoomInButton.setOnAction(event -> {
            actionManager.run(
                actionManager.getActionFactory().createZoomCenterAction(EditorViewModel.getDefaultZoomStep()));
        });
        String zoomInTooltip = "%s (%s)".formatted(ResourceHandler.getString("Tooltips", "zoom_in"),
            Shortcuts.ZOOM_IN.get().getDisplayText());
        zoomInButton.setTooltip(new Tooltip(zoomInTooltip));

        Label zoomLabel = new Label();
        zoomLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            double zoom = editorViewModel.getZoomScale();
            return String.format("%.0f%%", zoom * 100);
        }, editorViewModel.getZoomScaleProperty()));

        Button zoomOutButton = createStyledButton();
        zoomOutButton.getStyleClass().add(ZOOM_OUT_STYLE_CLASS);
        zoomOutButton.setOnAction(event -> actionManager.run(
            actionManager.getActionFactory().createZoomCenterAction(1 / EditorViewModel.getDefaultZoomStep())));
        String zoomOutTooltip = "%s (%s)".formatted(ResourceHandler.getString("Tooltips", "zoom_out"),
            Shortcuts.ZOOM_OUT.get().getDisplayText());
        zoomOutButton.setTooltip(new Tooltip(zoomOutTooltip));

        zoomButtons.getChildren().addAll(zoomInButton, zoomLabel, zoomOutButton);
        return zoomButtons;
    }

    public Node buildCurrentViewLabel() {
        Label currentViewLabel = new Label();
        currentViewLabel.textProperty()
            .bind(Bindings.createStringBinding(() -> editorViewModel.getCurrentSystem().getName(),
                editorViewModel.getCurrentSystem().getNameProperty()));
        return currentViewLabel;
    }

    public Node buildSearchWindow(EditorView editorView) {
        ToolBar searchBar = new ToolBar();

        // Close Search:
        Button closeButton = new Button(CLOSE_BUTTON);
        closeButton.setCancelButton(true);

        // Navigate Search:
        Button backwardButton = new Button(LEFT_BUTTON);
        backwardButton.setDisable(true);

        Button forwardButton = new Button(RIGHT_BUTTON);
        forwardButton.setDisable(true);

        Label matchesLabel = new Label();
        matchesLabel.setTextFill(Color.BLACK);

        final List<PositionableViewModelElement<?>> matches = new ArrayList<>();
        TextField searchTextField = new TextField();
        searchTextField.setPromptText(ResourceHandler.getString("Labels", "search"));

        searchBar.getItems().addAll(closeButton, searchTextField, backwardButton, forwardButton, matchesLabel);

        searchTextField.setOnAction(e -> {
            editorViewModel.getSelectionManager().deselectAll();
            List<PositionableViewModelElement<?>> oldSearchMatches = new ArrayList<>(matches);
            oldSearchMatches.forEach(matches::remove);
            matches.addAll(editorViewModel.getElementsByName(searchTextField.getText()));

            if (!matches.isEmpty()) {
                actionManager.run(
                    actionManager.getActionFactory().createFocusPositionableViewModelElementAction(matches.getFirst()));
                matchesLabel.setText(
                    String.format(ResourceHandler.getString("Labels", "matches_format_string"), 1, matches.size()));
                backwardButton.setDisable(true);
                forwardButton.setDisable(matches.size() == 1);
            } else {
                matchesLabel.setText(String.format(ResourceHandler.getString("Labels", "matches_format_string"), 0, 0));
                backwardButton.setDisable(true);
                forwardButton.setDisable(true);
            }
        });

        searchBar.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                searchTextField.requestFocus();
            }
        });

        forwardButton.setOnAction(e -> {
            if (!matches.isEmpty()) {
                searchNextResult(matches, matchesLabel, backwardButton, forwardButton, 1);
            }
        });

        closeButton.setOnAction(e -> {
            searchTextField.setText("");
            matchesLabel.setText("");
            editorView.activateSearchWindow(false);
        });

        return searchBar;
    }

    private void searchNextResult(
        List<PositionableViewModelElement<?>> matches, Label matchesLabel, Button backwardButton, Button forwardButton,
        int direction) {
        int currentPosition = matches.indexOf(editorViewModel.getFocusedElement());
        actionManager.run(actionManager.getActionFactory()
            .createFocusPositionableViewModelElementAction(matches.get(currentPosition + direction)));
        currentPosition += direction;
        matchesLabel.setText(
            String.format(ResourceHandler.getString("Labels", "matches_format_string"), currentPosition + 1,
                matches.size()));
        backwardButton.setDisable(currentPosition == 0);
        forwardButton.setDisable(currentPosition == matches.size() - 1);
    }

    public Node buildViewSwitchButtons() {
        HBox viewSwitchButtons = new HBox();
        final String switchToSystemStyleClass = "floating-switch-to-system-button";
        final String switchToAutomatonStyleClass = "floating-switch-to-automaton-button";
        final String switchToParentSystemStyleClass = "floating-parent-system-switch-button";

        Button switchViewButton = createStyledButton();
        switchViewButton.getStyleClass()
            .add(editorViewModel.isAutomatonEditor() ? switchToSystemStyleClass : switchToAutomatonStyleClass);
        switchViewButton.setOnAction(event -> {
            boolean automatonEditor = editorViewModel.isAutomatonEditor();
            actionManager.run(actionManager.getActionFactory()
                .createViewSwitchAction(editorViewModel.getCurrentSystem(), !automatonEditor));
            switchViewButton.getStyleClass()
                .remove(automatonEditor ? switchToAutomatonStyleClass : switchToSystemStyleClass);
            switchViewButton.getStyleClass()
                .add(automatonEditor ? switchToSystemStyleClass : switchToAutomatonStyleClass);
        });
        String switchViewTooltip = "%s (%s)".formatted(ResourceHandler.getString("Tooltips", "switch_view"),
            Shortcuts.SWITCH_EDITOR.get().getDisplayText());
        switchViewButton.setTooltip(new Tooltip(switchViewTooltip));

        viewSwitchButtons.getChildren().add(switchViewButton);

        if (editorViewModel.getParentSystem() != null) {
            Button parentSystemSwitchButton = createStyledButton();
            parentSystemSwitchButton.getStyleClass().add(switchToParentSystemStyleClass);
            parentSystemSwitchButton.setOnAction(event -> actionManager.run(actionManager.getActionFactory()
                .createViewSwitchAction(editorViewModel.getParentSystem(), editorViewModel.isAutomatonEditor())));
            String parentSystemSwitchTooltip =
                "%s (%s)".formatted(ResourceHandler.getString("Tooltips", "parent_system"),
                    Shortcuts.OPEN_PARENT_SYSTEM_EDITOR.get().getDisplayText());
            parentSystemSwitchButton.setTooltip(new Tooltip(parentSystemSwitchTooltip));

            viewSwitchButtons.getChildren().add(parentSystemSwitchButton);
        }


        return viewSwitchButtons;
    }

    private Button createStyledButton() {
        Button button = new Button();
        button.getStyleClass().add(FLOATING_BUTTON_STYLE_CLASS);
        button.setPrefSize(DEFAULT_BUTTON_SIZE, DEFAULT_BUTTON_SIZE);
        return button;
    }
}
