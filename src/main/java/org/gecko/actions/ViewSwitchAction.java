package org.gecko.actions;

import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class ViewSwitchAction extends Action {
    private final SystemViewModel systemViewModel;
    private final boolean isAutomaton;
    private final GeckoViewModel geckoViewModel;
    private final EditorViewModel oldEditor;

    ViewSwitchAction(GeckoViewModel geckoViewModel, SystemViewModel systemViewModel, boolean isAutomaton) {
        this.systemViewModel = systemViewModel;
        this.isAutomaton = isAutomaton;
        this.geckoViewModel = geckoViewModel;
        this.oldEditor = geckoViewModel.getCurrentEditor();
    }

    @Override
    void run() {
        geckoViewModel.switchEditor(systemViewModel, isAutomaton);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createViewSwitchAction(oldEditor.getCurrentSystem(), oldEditor.isAutomatonEditor());
    }
}
