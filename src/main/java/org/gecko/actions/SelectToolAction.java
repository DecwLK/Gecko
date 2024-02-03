package org.gecko.actions;

import org.gecko.tools.ToolType;
import org.gecko.viewmodel.EditorViewModel;

public class SelectToolAction extends Action {
    private final EditorViewModel editorViewModel;
    private final ToolType tool;

    SelectToolAction(EditorViewModel editorViewModel, ToolType tool) {
        this.editorViewModel = editorViewModel;
        this.tool = tool;
    }

    @Override
    void run() {
        editorViewModel.setCurrentTool(tool);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
