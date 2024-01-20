package org.gecko.actions;

import org.gecko.exceptions.InvalidConnectingPointType;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class CreateSystemConnectionViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final PortViewModel source;
    private final PortViewModel destination;
    private SystemConnectionViewModel createdSystemConnectionViewModel;

    CreateSystemConnectionViewModelElementAction(GeckoViewModel geckoViewModel, PortViewModel source, PortViewModel destination) {
        this.geckoViewModel = geckoViewModel;
        this.source = source;
        this.destination = destination;
    }

    @Override
    void run() {
        SystemViewModel currentParentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem();
        try {
            createdSystemConnectionViewModel =
                geckoViewModel.getViewModelFactory().createSystemConnectionViewModelIn(currentParentSystem, source, destination);
        } catch (InvalidConnectingPointType e) {
            System.out.println(e.getMessage());
        }
        createdSystemConnectionViewModel.setSource(source);
        createdSystemConnectionViewModel.setDestination(destination);
        createdSystemConnectionViewModel.updateTarget();
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(createdSystemConnectionViewModel);
    }
}
