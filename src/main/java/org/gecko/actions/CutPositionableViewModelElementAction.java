package org.gecko.actions;

import java.util.ArrayList;
import org.gecko.viewmodel.GeckoViewModel;

public class CutPositionableViewModelElementAction extends ActionGroup {

    GeckoViewModel geckoViewModel;
    Action delete;

    CutPositionableViewModelElementAction(GeckoViewModel geckoViewModel) {
        super(new ArrayList<>());
        this.geckoViewModel = geckoViewModel;
        var copy = new CopyPositionableViewModelElementAction(geckoViewModel);
        getActions().add(copy);
        delete = new DeletePositionableViewModelElementAction(geckoViewModel, geckoViewModel.getCurrentEditor().getSelectionManager().getCurrentSelection());
        getActions().add(delete);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return delete.getUndoAction(actionFactory);
    }
}
