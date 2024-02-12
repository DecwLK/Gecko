package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.SystemViewModel;

/**
 * A concrete representation of an {@link Action} that switches between {@link EditorViewModel}s depending on whether
 * the current {@link EditorViewModel} is an automaton view.
 */
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
    boolean run() throws GeckoException {
        geckoViewModel.switchEditor(systemViewModel, isAutomaton);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createViewSwitchAction(oldEditor.getCurrentSystem(), oldEditor.isAutomatonEditor());
    }
}
