package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.SelectionManager;

/**
 * A concrete representation of an {@link Action} that navigates back in the {@link SelectionManager}.
 */
public class SelectionHistoryBackAction extends Action {
    private final SelectionManager selectionManager;

    SelectionHistoryBackAction(SelectionManager selectionManager) {
        this.selectionManager = selectionManager;
    }

    @Override
    boolean run() throws GeckoException {
        this.selectionManager.goBack();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
