package org.gecko.actions;

import java.util.List;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class PastePositionableViewModelElementAction extends Action {
    PastePositionableViewModelElementAction(GeckoViewModel geckoViewModel) {
    }

    PastePositionableViewModelElementAction(
        GeckoViewModel geckoViewModel, List<PositionableViewModelElement<?>> elements) {
    }

    @Override
    boolean run() throws GeckoException {
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
