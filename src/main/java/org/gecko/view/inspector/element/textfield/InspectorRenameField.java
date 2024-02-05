package org.gecko.view.inspector.element.textfield;

import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.Renamable;

public class InspectorRenameField extends InspectorTextField {
    private final Renamable renamable;
    private final ActionManager actionManager;

    public InspectorRenameField(ActionManager actionManager, Renamable renamable) {
        super(renamable.getNameProperty(), actionManager);
        this.renamable = renamable;
        this.actionManager = actionManager;
    }

    @Override
    protected Action getAction() {
        return actionManager.getActionFactory().createRenameViewModelElementAction(renamable, getText());
    }
}
