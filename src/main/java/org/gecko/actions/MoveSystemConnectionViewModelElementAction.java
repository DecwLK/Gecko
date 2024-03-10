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

/**
 * A concrete representation of an {@link Action} that moves a {@link SystemConnectionViewModel} with a given
 * {@link Point2D delta value}.
 */
public class MoveSystemConnectionViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final EditorViewModel editorViewModel;
    private final SystemConnectionViewModel systemConnectionViewModel;
    private final ElementScalerBlock elementScalerBlock;
    private Point2D delta;
    private PortViewModel portViewModel;
    private PortViewModel previousPortViewModel;
    private boolean isVariableBlock = false;
    private boolean wasVariableBlock = false;

    private static final int PLUS = 1;
    private static final int MINUS = -1;
    private static final double HALF = 0.5;

    MoveSystemConnectionViewModelElementAction(
        GeckoViewModel geckoViewModel, SystemConnectionViewModel systemConnectionViewModel,
        ElementScalerBlock elementScalerBlock, Point2D delta) {
        this.geckoViewModel = geckoViewModel;
        this.editorViewModel = geckoViewModel.getCurrentEditor();
        this.systemConnectionViewModel = systemConnectionViewModel;
        this.elementScalerBlock = elementScalerBlock;
        this.delta = delta;
    }

    MoveSystemConnectionViewModelElementAction(
        GeckoViewModel geckoViewModel, SystemConnectionViewModel systemConnectionViewModel,
        ElementScalerBlock elementScalerBlock, PortViewModel portViewModel, boolean isVariableBlock) {
        this.geckoViewModel = geckoViewModel;
        this.editorViewModel = geckoViewModel.getCurrentEditor();
        this.systemConnectionViewModel = systemConnectionViewModel;
        this.elementScalerBlock = elementScalerBlock;
        this.portViewModel = portViewModel;
        this.isVariableBlock = isVariableBlock;
    }

    @Override
    boolean run() throws GeckoException {
        if (portViewModel == null) {
            portViewModel = getPortViewModelAt();
            if (portViewModel == null) {
                return false;
            }
        }

        SystemViewModel parentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem();
        PortViewModel sourcePortViewModel = systemConnectionViewModel.getSource();
        PortViewModel destinationPortViewModel = systemConnectionViewModel.getDestination();

        if (isSource()) {
            if (portViewModel.equals(sourcePortViewModel)) {
                return false;
            }
            wasVariableBlock = geckoViewModel.getSystemViewModelWithPort(sourcePortViewModel).equals(parentSystem);
            sourcePortViewModel = portViewModel;
        } else {
            if (portViewModel.equals(destinationPortViewModel)) {
                return false;
            }
            wasVariableBlock = geckoViewModel.getSystemViewModelWithPort(destinationPortViewModel).equals(parentSystem);
            destinationPortViewModel = portViewModel;
        }

        SystemViewModel sourceSystem = geckoViewModel.getSystemViewModelWithPort(sourcePortViewModel);
        SystemViewModel destinationSystem = geckoViewModel.getSystemViewModelWithPort(destinationPortViewModel);

        if (!SystemConnectionViewModel.isConnectingAllowed(sourcePortViewModel, destinationPortViewModel, sourceSystem,
            destinationSystem, parentSystem, systemConnectionViewModel)) {
            return false;
        }

        Property<Point2D> newPositionProperty;

        if (isVariableBlock) {
            newPositionProperty = new SimpleObjectProperty<>(
                calculateEndPortPosition(portViewModel.getPosition(), portViewModel.getSize(),
                    portViewModel.getVisibility(), false));

            portViewModel.getPositionProperty().addListener((observable, oldValue, newValue) -> {
                newPositionProperty.setValue(
                    calculateEndPortPosition(portViewModel.getPosition(), portViewModel.getSize(),
                        portViewModel.getVisibility(), false));
            });

        } else {
            newPositionProperty = new SimpleObjectProperty<>(
                calculateEndPortPosition(portViewModel.getSystemPortPositionProperty().getValue(),
                    portViewModel.getSystemPortSizeProperty().getValue(), portViewModel.getVisibility(), true));

            portViewModel.getSystemPortPositionProperty()
                .addListener((observable, oldValue, newValue) -> newPositionProperty.setValue(
                    calculateEndPortPosition(portViewModel.getSystemPortPositionProperty().getValue(),
                        portViewModel.getSystemPortSizeProperty().getValue(), portViewModel.getVisibility(), true)));
        }

        systemConnectionViewModel.getEdgePoints().set(elementScalerBlock.getIndex(), newPositionProperty);

        if (isSource()) {
            previousPortViewModel = systemConnectionViewModel.getSource();
            systemConnectionViewModel.setSource(sourcePortViewModel);
        } else {
            previousPortViewModel = systemConnectionViewModel.getDestination();
            systemConnectionViewModel.setDestination(destinationPortViewModel);
        }

        elementScalerBlock.updatePosition();
        systemConnectionViewModel.updateTarget();

        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createMoveSystemConnectionViewModelElementAction(systemConnectionViewModel,
            elementScalerBlock, previousPortViewModel, wasVariableBlock);
    }

    private PortViewModel getPortViewModelAt() {
        if (systemConnectionViewModel == null) {
            return null;
        }

        return getPortViewModelAt(elementScalerBlock.getLayoutPosition().add(delta));
    }

    private PortViewModel getPortViewModelAt(Point2D point) {
        // Check for variable blocks in the current system
        for (Variable variable : editorViewModel.getCurrentSystem().getTarget().getVariables()) {
            PortViewModel portViewModel = (PortViewModel) geckoViewModel.getViewModelElement(variable);
            if (point.getX() > portViewModel.getPosition().getX()
                && point.getX() < portViewModel.getPosition().getX() + portViewModel.getSize().getX()
                && point.getY() > portViewModel.getPosition().getY()
                && point.getY() < portViewModel.getPosition().getY() + portViewModel.getSize().getY()) {
                isVariableBlock = true;
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
                    isVariableBlock = false;
                    return portViewModel;
                }
            }
        }
        return null;
    }

    private Point2D calculateEndPortPosition(Point2D position, Point2D size, Visibility visibility, boolean isPort) {
        int sign = isPort ? PLUS : MINUS;
        return position.add(size.multiply(HALF))
            .subtract((visibility == Visibility.INPUT ? PLUS : MINUS) * sign * size.getX() / 2, 0);
    }

    private boolean isSource() {
        return elementScalerBlock.getIndex() == 0;
    }
}
