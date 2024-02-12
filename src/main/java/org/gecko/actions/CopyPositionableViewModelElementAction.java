package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.GeckoViewModel;

public class CopyPositionableViewModelElementAction extends Action {
    GeckoViewModel geckoViewModel;

    CopyPositionableViewModelElementAction(GeckoViewModel geckoViewModel) {
        this.geckoViewModel = geckoViewModel;
    }

    @Override
    boolean run() throws GeckoException {
        CopyPositionableViewModelElementVisitor visitor = new CopyPositionableViewModelElementVisitor(geckoViewModel);

        geckoViewModel.getCurrentEditor()
            .getSelectionManager()
            .getCurrentSelection()
            .forEach(element -> element.accept(visitor));

        geckoViewModel.getActionManager().setCopyVisitor(visitor);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
