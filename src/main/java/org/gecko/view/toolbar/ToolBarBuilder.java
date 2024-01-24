package org.gecko.view.toolbar;

import java.util.List;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import org.gecko.actions.ActionManager;
import org.gecko.tools.Tool;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.EditorViewModel;

public class ToolBarBuilder {

    private static final String DEFAULT_TOOLBAR_ICON_STYLE_NAME = "toolbar-icon";
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
    }

    private void addTools(ActionManager actionManager, ToggleGroup toggleGroup, List<Tool> toolList) {
        for (Tool tool : toolList) {
            ToggleButton toolButton = new ToggleButton(tool.getName());

            toolButton.setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
            toolButton.setMaxSize(BUTTON_SIZE, BUTTON_SIZE);
            toolButton.setMinSize(BUTTON_SIZE, BUTTON_SIZE);

            toolButton.setOnMouseClicked(event -> {
                actionManager.run(actionManager.getActionFactory().createSelectToolAction(editorView, tool));
            });

            toolButton.getStyleClass().add(DEFAULT_TOOLBAR_ICON_STYLE_NAME);
            toolButton.getStyleClass().add(tool.getIconStyleName());
            toolBar.getItems().add(toolButton);
            toggleGroup.getToggles().add(toolButton);
        }
    }

    public ToolBar build() {
        return toolBar;
    }
}
