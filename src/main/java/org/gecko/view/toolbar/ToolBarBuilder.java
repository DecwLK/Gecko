package org.gecko.view.toolbar;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import org.gecko.actions.ActionManager;
import org.gecko.tools.Tool;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.EditorViewModel;

import java.util.List;

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

    private void addTools(
            ActionManager actionManager, EditorViewModel editorViewModel, List<Tool> toolList) {
        for (Tool tool : toolList) {
            ButtonBase toolButton = new Button(tool.getName());

            toolButton.setOnAction(
                    event -> {
                        actionManager.run(
                                actionManager
                                        .getActionFactory()
                                        .createSelectToolAction(editorViewModel, tool));
                    });

            toolBar.getItems().add(toolButton);
        }
    }

    public ToolBar build() {
        return toolBar;
    }
}
