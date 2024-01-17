package org.gecko.actions;

import org.gecko.tools.Tool;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.EditorViewModel;

public class SelectToolAction extends Action {

    private final EditorView editorView;
    private final EditorViewModel editorViewModel;
    private final Tool tool;

    private final Tool previousTool;

    SelectToolAction(EditorView editorView, EditorViewModel editorViewModel, Tool tool) {
        this.editorView = editorView;
        this.editorViewModel = editorViewModel;
        this.tool = tool;

        this.previousTool = editorViewModel.getCurrentTool();
    }

    @Override
    void run() {
        editorViewModel.setCurrentTool(tool);
        editorView.acceptTool(tool);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createSelectToolAction(editorView, previousTool);
    }
}
