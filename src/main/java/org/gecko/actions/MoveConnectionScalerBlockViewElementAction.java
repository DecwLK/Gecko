package org.gecko.actions;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import org.gecko.model.State;
import org.gecko.model.System;
import org.gecko.model.Variable;
import org.gecko.model.Visibility;
import org.gecko.view.views.viewelement.decorator.ElementScalerBlock;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;

public class MoveConnectionScalerBlockViewElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final EditorViewModel editorViewModel;
    private final EdgeViewModel edgeViewModel;
    private final SystemConnectionViewModel systemConnectionViewModel;
    private final ElementScalerBlock elementScalerBlock;
    private final Point2D delta;

    private boolean validAction = false;

    MoveConnectionScalerBlockViewElementAction(
        GeckoViewModel geckoViewModel, EdgeViewModel edgeViewModel, ElementScalerBlock elementScalerBlock,
        Point2D delta) {
        this.geckoViewModel = geckoViewModel;
        this.editorViewModel = geckoViewModel.getCurrentEditor();
        this.edgeViewModel = edgeViewModel;
        this.systemConnectionViewModel = null;
        this.elementScalerBlock = elementScalerBlock;
        this.delta = delta;
    }

    MoveConnectionScalerBlockViewElementAction(
        GeckoViewModel geckoViewModel, SystemConnectionViewModel systemConnectionViewModel,
        ElementScalerBlock elementScalerBlock, Point2D delta) {
        this.geckoViewModel = geckoViewModel;
        this.editorViewModel = geckoViewModel.getCurrentEditor();
        this.edgeViewModel = null;
        this.systemConnectionViewModel = systemConnectionViewModel;
        this.elementScalerBlock = elementScalerBlock;
        this.delta = delta;
    }

    @Override
    void run() {
        if (elementScalerBlock.getIndex() == 0) {
            StateViewModel newStateViewModel = attemptStateViewModelRelocation();
            if (newStateViewModel != null) {
                edgeViewModel.setSource(newStateViewModel);
            }

            PortViewModel newPortViewModel = attemptPortViewModelRelocation();
            if (newPortViewModel != null) {
                systemConnectionViewModel.setSource(newPortViewModel);
            }
        } else if (elementScalerBlock.getIndex() == elementScalerBlock.getDecoratorTarget().getScalers().size() - 1) {
            StateViewModel newStateViewModel = attemptStateViewModelRelocation();
            if (newStateViewModel != null) {
                edgeViewModel.setDestination(newStateViewModel);
            }

            PortViewModel newPortViewModel = attemptPortViewModelRelocation();
            if (newPortViewModel != null) {
                systemConnectionViewModel.setDestination(newPortViewModel);
            }
        }
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return (validAction) ? createUndoAction(actionFactory) : null;
    }

    private Action createUndoAction(ActionFactory actionFactory) {
        if (edgeViewModel != null) {
            return actionFactory.createMoveConnectionScalerBlockViewElementAction(edgeViewModel, elementScalerBlock,
                delta.multiply(-1));
        } else {
            return actionFactory.createMoveConnectionScalerBlockViewElementAction(systemConnectionViewModel,
                elementScalerBlock, delta.multiply(-1));
        }
    }

    private PortViewModel attemptPortViewModelRelocation() {
        if (systemConnectionViewModel == null) {
            return null;
        }

        PortViewModel portViewModel = getPortViewModelAt(elementScalerBlock.getPoint().add(delta));
        if (portViewModel != null) {
            validAction = true;
        }

        return portViewModel;
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

    private StateViewModel attemptStateViewModelRelocation() {
        if (edgeViewModel == null) {
            return null;
        }

        StateViewModel stateViewModel = getStateViewModelAt(elementScalerBlock.getPoint().add(delta));
        if (stateViewModel != null) {
            validAction = true;
            elementScalerBlock.setPoint(stateViewModel.getCenter());
        }

        return stateViewModel;
    }

    private StateViewModel getStateViewModelAt(Point2D point) {
        for (State state : editorViewModel.getCurrentSystem().getTarget().getAutomaton().getStates()) {
            StateViewModel stateViewModel = (StateViewModel) geckoViewModel.getViewModelElement(state);
            if (point.getX() > stateViewModel.getPosition().getX()
                && point.getX() < stateViewModel.getPosition().getX() + stateViewModel.getSize().getX()
                && point.getY() > stateViewModel.getPosition().getY()
                && point.getY() < stateViewModel.getPosition().getY() + stateViewModel.getSize().getY()) {
                return stateViewModel;
            }
        }

        return null;
    }
}
