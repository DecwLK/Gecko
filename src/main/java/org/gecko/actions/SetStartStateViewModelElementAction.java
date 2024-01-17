package org.gecko.actions;

import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.StateViewModel;

public class SetStartStateViewModelElementAction extends Action {
    private final EditorViewModel editorViewModel;
    private final StateViewModel stateViewModel;

    SetStartStateViewModelElementAction(EditorViewModel editorViewModel, StateViewModel stateViewModel) {
        this.editorViewModel = editorViewModel;
        this.stateViewModel = stateViewModel;
    }

    @Override
    void run() {
        this.editorViewModel.getCurrentSystem().getTarget().getAutomaton().setStartState(stateViewModel.getTarget());
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
