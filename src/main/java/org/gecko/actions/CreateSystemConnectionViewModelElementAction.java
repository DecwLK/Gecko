package org.gecko.actions;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import org.gecko.exceptions.GeckoException;
import org.gecko.model.Visibility;
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
        if (destination.getTarget().isHasIncomingConnection()) {
            return false;
        }

        Property<Point2D> sourcePosition = new SimpleObjectProperty<>(sourceIsPort ?
            calculateEndPortPosition(source.getSystemPortPositionProperty().getValue(),
                source.getSystemPortSizeProperty().getValue(), source.getVisibility()) :
            calculateEndPortPosition(source.getPosition(), source.getSize(), source.getVisibility()));

        // position the line at the tip of the port
        if (sourceIsPort) {
            source.getSystemPortPositionProperty()
                .addListener((observable, oldValue, newValue) -> sourcePosition.setValue(
                    calculateEndPortPosition(source.getSystemPortPositionProperty().getValue(),
                        source.getSystemPortSizeProperty().getValue(), source.getVisibility())));
        } else {
            source.getPositionProperty().addListener((observable, oldValue, newValue) -> {
                sourcePosition.setValue(
                    calculateEndPortPosition(source.getPosition(), source.getSize(), source.getVisibility()));
            });
        }

        Property<Point2D> destinationPosition = new SimpleObjectProperty<>(destIsPort ?
            calculateEndPortPosition(destination.getSystemPortPositionProperty().getValue(),
                destination.getSystemPortSizeProperty().getValue(), destination.getVisibility()) :
            calculateEndPortPosition(destination.getPosition(), destination.getSize(), destination.getVisibility()));

        if (destIsPort) {
            destination.getSystemPortPositionProperty()
                .addListener((observable, oldValue, newValue) -> destinationPosition.setValue(
                    calculateEndPortPosition(destination.getSystemPortPositionProperty().getValue(),
                        destination.getSystemPortSizeProperty().getValue(), destination.getVisibility())));
        } else {
            destination.getPositionProperty().addListener((observable, oldValue, newValue) -> {
                destinationPosition.setValue(calculateEndPortPosition(destination.getPosition(), destination.getSize(),
                    destination.getVisibility()));
            });
        }

        SystemViewModel currentParentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem();
        SystemViewModel sourceSystem = (SystemViewModel) geckoViewModel.getViewModelElement(
            currentParentSystem.getTarget().getChildSystemWithVariable(source.getTarget()));
        if (sourceSystem == null && currentParentSystem.getPorts().contains(source)) {
            sourceSystem = currentParentSystem;
        }
        SystemViewModel destinationSystem = (SystemViewModel) geckoViewModel.getViewModelElement(
            currentParentSystem.getTarget().getChildSystemWithVariable(destination.getTarget()));
        if (destinationSystem == null && currentParentSystem.getPorts().contains(destination)) {
            destinationSystem = currentParentSystem;
        }
        if (sourceSystem == null || destinationSystem == null || sourceSystem == destinationSystem) {
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

    private Point2D calculateEndPortPosition(Point2D position, Point2D size, Visibility visibility) {
        return position.add(size.multiply(0.5))
            .subtract((visibility == Visibility.INPUT ? 1 : -1) * size.getX() / 2, 0);
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
