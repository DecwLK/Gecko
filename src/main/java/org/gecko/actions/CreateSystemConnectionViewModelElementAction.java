package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import javafx.beans.property.Property;
import javafx.geometry.Point2D;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class CreateSystemConnectionViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final PortViewModel source;
    private final PortViewModel destination;
    private SystemConnectionViewModel createdSystemConnectionViewModel;

    private final boolean sourceIsPort;
    private final boolean destIsPort;

    CreateSystemConnectionViewModelElementAction(
        GeckoViewModel geckoViewModel, PortViewModel source, PortViewModel destination) {
        this.geckoViewModel = geckoViewModel;
        this.source = source;
        this.destination = destination;
        this.sourceIsPort = isPort(source);
        this.destIsPort = isPort(destination);
    }

    @Override
    boolean run() throws GeckoException {
        Property<Point2D> sourcePosition =
            sourceIsPort ? source.getSystemPortPositionProperty() : source.getPositionProperty();
        Property<Point2D> destinationPosition =
            destIsPort ? destination.getSystemPortPositionProperty() : destination.getPositionProperty();

        SystemViewModel currentParentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem();
        SystemViewModel sourceSystem = (SystemViewModel) geckoViewModel.getViewModelElement(
            currentParentSystem.getTarget().getChildSystemWithVariable(source.getTarget()));
        SystemViewModel destinationSystem = (SystemViewModel) geckoViewModel.getViewModelElement(
            currentParentSystem.getTarget().getChildSystemWithVariable(destination.getTarget()));
        if (sourceSystem == null || destinationSystem == null || sourceSystem == destinationSystem) {
            return false;
        }

        if (destination.getTarget().isHasIncomingConnection()) {
            return false;
        }

        createdSystemConnectionViewModel = geckoViewModel.getViewModelFactory()
            .createSystemConnectionViewModelIn(currentParentSystem, source, destination);

        createdSystemConnectionViewModel.getEdgePoints().add(sourcePosition);
        createdSystemConnectionViewModel.getEdgePoints().add(destinationPosition);
        createdSystemConnectionViewModel.updateTarget();
        ActionManager actionManager = geckoViewModel.getActionManager();
        actionManager.run(actionManager.getActionFactory().createSelectAction(createdSystemConnectionViewModel, true));
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(createdSystemConnectionViewModel);
    }

    private boolean isPort(PortViewModel portViewModel) {
        return geckoViewModel.getCurrentEditor()
            .getCurrentSystem()
            .getTarget()
            .getVariables()
            .stream()
            .filter(variable -> portViewModel.getTarget().equals(variable))
            .findFirst()
            .isEmpty();
    }
}
