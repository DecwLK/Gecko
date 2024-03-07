package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.exceptions.GeckoException;
import org.gecko.model.State;
import org.gecko.view.views.viewelement.decorator.ElementScalerBlock;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;

/**
 * A concrete representation of an {@link Action} that moves an {link EdgeViewModelElement} with a given
 * {@link Point2D delta value}.
 */
public class MoveEdgeViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final EditorViewModel editorViewModel;
    private final EdgeViewModel edgeViewModel;
    private final ElementScalerBlock elementScalerBlock;
    private Point2D delta;
    private StateViewModel stateViewModel;
    private StateViewModel previousStateViewModel;
    private ContractViewModel contractViewModel;
    private ContractViewModel previousContractViewModel;

    MoveEdgeViewModelElementAction(
        GeckoViewModel geckoViewModel, EdgeViewModel edgeViewModel, ElementScalerBlock elementScalerBlock,
        Point2D delta) {
        this.geckoViewModel = geckoViewModel;
        this.editorViewModel = geckoViewModel.getCurrentEditor();
        this.edgeViewModel = edgeViewModel;
        this.elementScalerBlock = elementScalerBlock;
        this.delta = delta;
    }

    MoveEdgeViewModelElementAction(
        GeckoViewModel geckoViewModel, EdgeViewModel edgeViewModel, ElementScalerBlock elementScalerBlock,
        StateViewModel stateViewModel, ContractViewModel contractViewModel) {
        this.geckoViewModel = geckoViewModel;
        this.editorViewModel = geckoViewModel.getCurrentEditor();
        this.edgeViewModel = edgeViewModel;
        this.elementScalerBlock = elementScalerBlock;
        this.stateViewModel = stateViewModel;
        this.contractViewModel = contractViewModel;
    }


    @Override
    boolean run() throws GeckoException {
        previousStateViewModel =
            elementScalerBlock.getIndex() == 0 ? edgeViewModel.getSource() : edgeViewModel.getDestination();
        if (stateViewModel == null) {
            stateViewModel = attemptRelocation();
            if (stateViewModel == null) {
                edgeViewModel.setBindings();
                return false;
            }
        }

        if (elementScalerBlock.getIndex() == 0) {
            edgeViewModel.setSource(stateViewModel);
            previousContractViewModel = edgeViewModel.getContract();
            edgeViewModel.setContract(contractViewModel);
        } else {
            edgeViewModel.setDestination(stateViewModel);
        }

        elementScalerBlock.updatePosition();
        edgeViewModel.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createMoveEdgeViewModelElementAction(edgeViewModel, elementScalerBlock,
            previousStateViewModel, previousContractViewModel);
    }

    private StateViewModel attemptRelocation() {
        return getStateViewModelAt(elementScalerBlock.getLayoutPosition().add(delta));
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
