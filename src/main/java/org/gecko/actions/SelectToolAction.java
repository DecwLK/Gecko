package org.gecko.actions;

import org.gecko.tools.ToolType;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.EditorViewModel;

public class SelectToolAction extends Action {
    private final EditorView editorView;
    private final EditorViewModel editorViewModel;
    private final ToolType tool;

    private final ToolType previousTool;

    SelectToolAction(EditorView editorView, EditorViewModel editorViewModel, ToolType tool) {
        this.editorView = editorView;
        this.editorViewModel = editorViewModel;
        this.tool = tool;

        this.previousTool = editorViewModel.getCurrentToolType();
    }

    @Override
    void run() {
        editorViewModel.setCurrentTool(tool);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createSelectToolAction(editorView, previousTool);
    }
}
