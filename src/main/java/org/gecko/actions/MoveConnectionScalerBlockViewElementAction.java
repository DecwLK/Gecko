package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.model.State;
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
        } else {
            elementScalerBlock.setPoint(elementScalerBlock.getPoint().add(delta));
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
            elementScalerBlock.setPoint(portViewModel.getCenter());
        }

        return portViewModel;
    }

    private PortViewModel getPortViewModelAt(Point2D point) {
        return null;
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
