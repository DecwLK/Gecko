package org.gecko.view.toolbar;

import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import org.gecko.actions.ActionManager;
import org.gecko.tools.Tool;
import org.gecko.viewmodel.EditorViewModel;

public class ToolBarBuilder {

    private final ToolBar toolBar;

    public ToolBarBuilder(ActionManager actionManager, EditorViewModel editorViewModel) {
        this.toolBar = new ToolBar();

        for (List<Tool> toolList : editorViewModel.getTools()) {
            addTools(actionManager, editorViewModel, toolList);

            // add separator
            toolBar.getItems().add(new Separator());
        }
    }

    private void addTools(ActionManager actionManager, EditorViewModel editorViewModel, List<Tool> toolList) {
        for (Tool tool : toolList) {
            ButtonBase toolButton = new Button(tool.getName());

            toolButton.setOnAction(event -> {
                actionManager.run(actionManager.getActionFactory().createSelectToolAction(tool));
            });

            toolBar.getItems().add(toolButton);
        }
    }

    public ToolBar build() {
        return toolBar;
    }
}
