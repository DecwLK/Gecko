package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.exceptions.GeckoException;
import org.gecko.model.State;
import org.gecko.view.views.viewelement.decorator.ElementScalerBlock;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;

public class MoveEdgeScalerBlockViewElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final EditorViewModel editorViewModel;
    private final EdgeViewModel edgeViewModel;
    private final ElementScalerBlock elementScalerBlock;
    private final Point2D delta;

    private boolean successMove = false;

    MoveEdgeScalerBlockViewElementAction(
        GeckoViewModel geckoViewModel, EdgeViewModel edgeViewModel, ElementScalerBlock elementScalerBlock,
        Point2D delta) {
        this.geckoViewModel = geckoViewModel;
        this.editorViewModel = geckoViewModel.getCurrentEditor();
        this.edgeViewModel = edgeViewModel;
        this.elementScalerBlock = elementScalerBlock;
        this.delta = delta;
    }

    @Override
    boolean run() throws GeckoException {
        if (elementScalerBlock.getIndex() == 0) {
            StateViewModel newStateViewModel = attemptRelocation();
            if (newStateViewModel != null) {
                edgeViewModel.setSource(newStateViewModel);
            }
        } else if (elementScalerBlock.getIndex() == elementScalerBlock.getDecoratorTarget().getScalers().size() - 1) {
            StateViewModel newStateViewModel = attemptRelocation();
            if (newStateViewModel != null) {
                edgeViewModel.setDestination(newStateViewModel);
            }
        } else {
            elementScalerBlock.setPoint(elementScalerBlock.getPoint().add(delta));
        }
        return true;
    }

    private StateViewModel attemptRelocation() {
        StateViewModel stateViewModel = getStateViewModelAt(elementScalerBlock.getPoint().add(delta));
        if (stateViewModel != null) {
            elementScalerBlock.setPoint(stateViewModel.getCenter());
        }

        successMove = stateViewModel != null;
        return stateViewModel;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createMoveEdgeScalerBlockViewElementAction(edgeViewModel, elementScalerBlock,
            delta.multiply(-1));
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
