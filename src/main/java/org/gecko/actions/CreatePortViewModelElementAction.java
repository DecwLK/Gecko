package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemViewModel;

/**
 * A concrete representation of an {@link Action} that creates a {@link PortViewModel} in a given
 * {@link SystemViewModel} through the {@link org.gecko.viewmodel.ViewModelFactory ViewModelFactory} of the
 * {@link GeckoViewModel}.
 */
public class CreatePortViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final SystemViewModel systemViewModel;
    private PortViewModel createdPortViewModel;

    CreatePortViewModelElementAction(GeckoViewModel geckoViewModel, SystemViewModel parentSystem) {
        this.geckoViewModel = geckoViewModel;
        this.systemViewModel = parentSystem;
    }

    @Override
    boolean run() throws GeckoException {
        createdPortViewModel = geckoViewModel.getViewModelFactory().createPortViewModelIn(systemViewModel);
        double offset = createdPortViewModel.getSize().getY() * (systemViewModel.getPorts().size() - 1);
        createdPortViewModel.setPosition(new Point2D(2, 2 + offset));
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(createdPortViewModel);
    }
}
