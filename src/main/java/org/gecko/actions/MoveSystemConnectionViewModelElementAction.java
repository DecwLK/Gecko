package org.gecko.actions;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import org.gecko.exceptions.GeckoException;
import org.gecko.model.System;
import org.gecko.model.Variable;
import org.gecko.model.Visibility;
import org.gecko.view.views.viewelement.decorator.ElementScalerBlock;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class MoveSystemConnectionViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final EditorViewModel editorViewModel;
    private final SystemConnectionViewModel systemConnectionViewModel;
    private final ElementScalerBlock elementScalerBlock;
    private final Point2D delta;

    MoveSystemConnectionViewModelElementAction(
        GeckoViewModel geckoViewModel, SystemConnectionViewModel systemConnectionViewModel,
        ElementScalerBlock elementScalerBlock, Point2D delta) {
        this.geckoViewModel = geckoViewModel;
        this.editorViewModel = geckoViewModel.getCurrentEditor();
        this.systemConnectionViewModel = systemConnectionViewModel;
        this.elementScalerBlock = elementScalerBlock;
        this.delta = delta;
    }

    @Override
    boolean run() throws GeckoException {
        PortViewModel newPortViewModel = attemptPortViewModelRelocation();
        if (newPortViewModel == null) {
            return false;
        }

        SystemViewModel parentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem();
        PortViewModel sourcePortViewModel = systemConnectionViewModel.getSource();
        PortViewModel destinationPortViewModel = systemConnectionViewModel.getDestination();

        if (isSource()) {
            sourcePortViewModel = newPortViewModel;
        } else {
            destinationPortViewModel = newPortViewModel;
        }

        SystemViewModel sourceSystem = geckoViewModel.getSystemViewModelWithPort(sourcePortViewModel);
        SystemViewModel destinationSystem = geckoViewModel.getSystemViewModelWithPort(destinationPortViewModel);

        if (!SystemConnectionViewModel.isConnectingAllowed(sourcePortViewModel, destinationPortViewModel, sourceSystem,
            destinationSystem, parentSystem)) {
            return false;
        }

        if (isSource()) {
            systemConnectionViewModel.setSource(sourcePortViewModel);
        } else {
            systemConnectionViewModel.setDestination(destinationPortViewModel);
        }
        systemConnectionViewModel.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createMoveSystemConnectionViewModelElementAction(systemConnectionViewModel,
            elementScalerBlock, delta.multiply(-1));
    }

    private PortViewModel attemptPortViewModelRelocation() {
        if (systemConnectionViewModel == null) {
            return null;
        }

        return getPortViewModelAt(elementScalerBlock.getPosition().add(delta));
    }

    private PortViewModel getPortViewModelAt(Point2D point) {
        // Check for variable blocks in the current system
        for (Variable variable : editorViewModel.getCurrentSystem().getTarget().getVariables()) {
            PortViewModel portViewModel = (PortViewModel) geckoViewModel.getViewModelElement(variable);
            if (point.getX() > portViewModel.getPosition().getX()
                && point.getX() < portViewModel.getPosition().getX() + portViewModel.getSize().getX()
                && point.getY() > portViewModel.getPosition().getY()
                && point.getY() < portViewModel.getPosition().getY() + portViewModel.getSize().getY()) {
                systemConnectionViewModel.getEdgePoints()
                    .set(elementScalerBlock.getIndex(), portViewModel.getPositionProperty());
                return portViewModel;
            }
        }

        // Check for ports in the children systems
        for (System system : editorViewModel.getCurrentSystem().getTarget().getChildren()) {
            for (Variable variable : system.getVariables()) {
                PortViewModel portViewModel = (PortViewModel) geckoViewModel.getViewModelElement(variable);
                if (point.getX() > portViewModel.getSystemPortPositionProperty().getValue().getX() && point.getX()
                    < portViewModel.getSystemPortPositionProperty().getValue().getX()
                    + portViewModel.getSystemPortSizeProperty().getValue().getX()
                    && point.getY() > portViewModel.getSystemPortPositionProperty().getValue().getY() && point.getY()
                    < portViewModel.getSystemPortPositionProperty().getValue().getY()
                    + portViewModel.getSystemPortSizeProperty().getValue().getY()) {

                    // create new position property at the tip of the port
                    Property<Point2D> newPositionProperty = new SimpleObjectProperty<>(
                        calculateEndPortPosition(portViewModel.getSystemPortPositionProperty().getValue(),
                            portViewModel.getSystemPortSizeProperty().getValue(), portViewModel.getVisibility()));

                    portViewModel.getSystemPortPositionProperty()
                        .addListener((observable, oldValue, newValue) -> newPositionProperty.setValue(
                            calculateEndPortPosition(portViewModel.getSystemPortPositionProperty().getValue(),
                                portViewModel.getSystemPortSizeProperty().getValue(), portViewModel.getVisibility())));

                    systemConnectionViewModel.getEdgePoints().set(elementScalerBlock.getIndex(), newPositionProperty);
                    return portViewModel;
                }
            }
        }
        return null;
    }

    private Point2D calculateEndPortPosition(Point2D position, Point2D size, Visibility visibility) {
        return position.add(size.multiply(0.5))
            .subtract((visibility == Visibility.INPUT ? 1 : -1) * size.getX() / 2, 0);
    }

    private boolean isSource() {
        return elementScalerBlock.getIndex() == 0;
    }
}
