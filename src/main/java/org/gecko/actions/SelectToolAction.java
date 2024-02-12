package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.tools.ToolType;
import org.gecko.viewmodel.EditorViewModel;

/**
 * A concrete representation of an {@link Action} that changes the active {@link org.gecko.tools.Tool} in the current
 * {@link EditorViewModel}.
 */
public class SelectToolAction extends Action {
    private final EditorViewModel editorViewModel;
    private final ToolType tool;

    SelectToolAction(EditorViewModel editorViewModel, ToolType tool) {
        this.editorViewModel = editorViewModel;
        this.tool = tool;
    }

    @Override
    boolean run() throws GeckoException {
        editorViewModel.setCurrentTool(tool);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
