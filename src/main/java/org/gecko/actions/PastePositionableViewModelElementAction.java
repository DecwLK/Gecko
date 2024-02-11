package org.gecko.actions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.gecko.exceptions.GeckoException;
import org.gecko.exceptions.MissingViewModelElementException;
import org.gecko.exceptions.ModelException;
import org.gecko.model.System;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;

public class PastePositionableViewModelElementAction extends Action {
    private final GeckoViewModel geckoViewModel;
    private final CopyPositionableViewModelElementVisitor copyVisitor;
    private final Set<PositionableViewModelElement<?>> pastedElements;
    private static final Point2D POSITION_OFFSET = new Point2D(20, 20);

    PastePositionableViewModelElementAction(GeckoViewModel geckoViewModel) {
        this.geckoViewModel = geckoViewModel;
        this.copyVisitor = geckoViewModel.getActionManager().getCopyVisitor();
        pastedElements = new HashSet<>();
    }

    @Override
    boolean run() throws GeckoException {
        if (copyVisitor == null) {
            throw new GeckoException("Invalid Clipboard. Nothing to paste.");
        }

        if (copyVisitor.isWrapperEmpty()) {
            Alert alert =
                new Alert(Alert.AlertType.ERROR, "Clipboard is empty, so there is nothing to paste.", ButtonType.OK);
            alert.showAndWait();
            return false;
        }

        if (geckoViewModel.getCurrentEditor().isAutomatonEditor() != copyVisitor.isAutomatonCopy()) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                "Cannot paste here, because copied content is incompatible with opened view.", ButtonType.OK);
            alert.showAndWait();
            return false;
        }

        ViewModelFactory factory = geckoViewModel.getViewModelFactory();
        System currentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget();
        if (copyVisitor.isAutomatonCopy()) {
            copyVisitor.getCopiedStates().forEach(state -> {
                currentSystem.getAutomaton().addState(state);
                geckoViewModel.addViewModelElement(factory.createStateViewModelFrom(state));
            });
            copyVisitor.getCopiedRegions().forEach(region -> {
                currentSystem.getAutomaton().addRegion(region);
                try {
                    geckoViewModel.addViewModelElement(factory.createRegionViewModelFrom(region));
                } catch (MissingViewModelElementException e) {
                    throw new RuntimeException(e);
                }
            });
            //pasteEdgeViewModels(factory);
        } else {
            copyVisitor.getCopiedSystems().forEach(system -> {
                system.setParent(currentSystem);
                currentSystem.addChild(system);
                factory.createSystemViewModelFrom(system);
                factory.createSystemViewModelForChildren(system);
                java.lang.System.out.println(system.getId());
            });
            copyVisitor.getCopiedPorts().forEach(variable -> {
                currentSystem.addVariable(variable);
                geckoViewModel.addViewModelElement(factory.createPortViewModelFrom(variable));
            });
            //pasteSystemConnectionViewModels(factory);
        }
        return true;
    }

    /*private void pasteEdgeViewModels(ViewModelFactory factory) throws GeckoException {
        for (CopiedEdgeWrapper copiedEdgeWrapper : copyVisitor.getCopiedEdges()) {
            if (states.get(copiedEdgeWrapper.getSource()) == null) {
                pasteStateViewModel(factory, copiedEdgeWrapper.getSource());
            }

            if (states.get(copiedEdgeWrapper.getDestination()) == null) {
                pasteStateViewModel(factory, copiedEdgeWrapper.getDestination());
            }

            EdgeViewModel newEdge =
                factory.createEdgeViewModelIn(currentSystem, states.get(copiedEdgeWrapper.getSource()),
                    states.get(copiedEdgeWrapper.getDestination()));

            if (copiedEdgeWrapper.getEdge().getContract() != null) {
                newEdge.setContract(copiedEdgeWrapper.getEdge().getContract());
            }

            newEdge.setKind(copiedEdgeWrapper.getEdge().getKind());
            newEdge.setPriority(copiedEdgeWrapper.getEdge().getPriority());
            newEdge.setPosition(copiedEdgeWrapper.getEdge().getPosition().add(POSITION_OFFSET));
            newEdge.setSize(copiedEdgeWrapper.getEdge().getSize());
            newEdge.updateTarget();

            pastedElements.add(newEdge);
        }
    }*/

    /*private void pasteSystemConnectionViewModels(ViewModelFactory factory) throws GeckoException {
        for (CopiedSystemConnectionWrapper copiedSystemConnection : copyVisitor.getCopiedSystemConnections()) {
            // Copied dependencies:
            PortViewModel copiedSource = copiedSystemConnection.getSource();
            PortViewModel copiedDestination = copiedSystemConnection.getDestination();

            SystemViewModel copiedSourceParent = copiedSystemConnection.getSourceParent();
            copiedSourceParent.getPorts().remove(copiedSource);
            // TODO: avoid pasting it, don;t actually remove it
            SystemViewModel copiedDestinationParent = copiedSystemConnection.getDestinationParent();
            copiedDestinationParent.getPorts().remove(copiedDestination);
            // TODO: avoid pasting it, don;t actually remove it

            // New dependencies:
            PortViewModel newSource = pastePortViewModel(factory, copiedSource);
            PortViewModel newDestination = pastePortViewModel(factory, copiedDestination);

            SystemViewModel newSourceParent = pasteSystemViewModel(factory, copiedSourceParent);
            newSourceParent.getPorts().add(newSource);

            SystemViewModel newDestinationParent = pasteSystemViewModel(factory, copiedDestinationParent);
            newDestinationParent.getPorts().add(newDestination);

            copiedSourceParent.getPorts().add(copiedSource);
            copiedDestinationParent.getPorts().add(copiedDestination);

            SystemConnectionViewModel newSystemConnection =
                factory.createSystemConnectionViewModelIn(currentSystem, newSource, newDestination);
            newSystemConnection.setPosition(
                copiedSystemConnection.getSystemConnection().getPosition().add(POSITION_OFFSET));
            newSystemConnection.setSize(copiedSystemConnection.getSystemConnection().getSize());
            newSystemConnection.updateTarget();

            pastedElements.add(newSource);
            pastedElements.add(newDestination);
            pastedElements.add(newSystemConnection);
        }
    }*/

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(pastedElements);
    }
}
