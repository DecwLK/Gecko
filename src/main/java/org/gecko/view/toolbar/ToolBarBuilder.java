package org.gecko.view.toolbar;

import java.util.List;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.tools.Tool;
import org.gecko.tools.ToolType;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.EditorViewModel;

/**
 * Represents a builder for the {@link ToolBar} displayed in the view, containing a {@link ToggleGroup} with
 * {@link ToggleButton}s for each of the current view's available {@link Tool}s, as well as {@link ToggleButton}s for
 * running the undo and redo operations. Holds a reference to the built {@link ToolBar} and the current
 * {@link EditorView}.
 */
public class ToolBarBuilder {

    private static final String DEFAULT_TOOLBAR_ICON_STYLE_NAME = "toolbar-icon";
    private static final String UNDO_ICON_STYLE_NAME = "undo-toolbar-icon";
    private static final String REDO_ICON_STYLE_NAME = "redo-toolbar-icon";
    private static final int BUTTON_SIZE = 30;

    private final ToolBar toolBar;
    private final EditorView editorView;

    public ToolBarBuilder(ActionManager actionManager, EditorView editorView, EditorViewModel editorViewModel) {
        this.toolBar = new ToolBar();
        this.editorView = editorView;
        toolBar.setOrientation(Orientation.VERTICAL);

        ToggleGroup toggleGroup = new ToggleGroup();

        toggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle == null) {
                toggleGroup.selectToggle(oldToggle);
            }
        });

        for (int i = 0; i < editorViewModel.getTools().size(); i++) {
            addTools(actionManager, toggleGroup, editorViewModel.getTools().get(i));

            // add separator
            if (i < editorViewModel.getTools().size() - 1) {
                toolBar.getItems().add(new Separator());
            }
        }

        // Undo and Redo buttons
        toolBar.getItems().add(new Separator());
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        toolBar.getItems().add(spacer);

        HBox undoButtonBox = new HBox();
        Button undoButton = new Button("Undo");
        undoButton.setOnAction(event -> actionManager.undo());
        undoButton.getStyleClass().add(DEFAULT_TOOLBAR_ICON_STYLE_NAME);
        undoButton.getStyleClass().add(UNDO_ICON_STYLE_NAME);
        undoButtonBox.setAlignment(Pos.CENTER);
        undoButtonBox.getChildren().add(undoButton);
        toolBar.getItems().add(undoButtonBox);

        HBox redoButtonBox = new HBox();
        Button redoButton = new Button("Redo");
        redoButton.setOnAction(event -> actionManager.redo());
        redoButton.getStyleClass().add(DEFAULT_TOOLBAR_ICON_STYLE_NAME);
        redoButton.getStyleClass().add(REDO_ICON_STYLE_NAME);
        redoButtonBox.setAlignment(Pos.CENTER);
        redoButtonBox.getChildren().add(redoButton);
        toolBar.getItems().add(redoButtonBox);
    }

    private void addTools(ActionManager actionManager, ToggleGroup toggleGroup, List<Tool> toolList) {
        for (Tool tool : toolList) {
            ToolType toolType = tool.getToolType();
            ToggleButton toolButton = new ToggleButton(toolType.getLabel());

            //PrefSize is important
            toolButton.setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
            toolButton.setMaxSize(BUTTON_SIZE, BUTTON_SIZE);
            toolButton.setMinSize(BUTTON_SIZE, BUTTON_SIZE);

            //Would like to bind the selectedproperty of the button here but cannot because of a javafx bug
            editorView.getViewModel().getCurrentToolProperty().addListener((observable, oldValue, newValue) -> {
                toolButton.setSelected(newValue == tool);
            });
            toolButton.setSelected(editorView.getViewModel().getCurrentToolType() == toolType);

            toolButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    Action action = actionManager.getActionFactory().createSelectToolAction(toolType);
                    actionManager.run(action);
                }
            });

            toolButton.getStyleClass().set(0, DEFAULT_TOOLBAR_ICON_STYLE_NAME);
            toolButton.getStyleClass().add(toolType.getIcon());
            Tooltip tooltip =
                new Tooltip(toolType.getLabel() + " (" + toolType.getKeyCodeCombination().getDisplayText() + ")");
            toolButton.setTooltip(tooltip);
            toolBar.getItems().add(toolButton);
            toggleGroup.getToggles().add(toolButton);
        }
    }

    public ToolBar build() {
        return toolBar;
    }
}
