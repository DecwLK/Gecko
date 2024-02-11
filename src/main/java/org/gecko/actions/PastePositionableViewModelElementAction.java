package org.gecko.actions;

import java.util.HashSet;
import java.util.Set;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.gecko.exceptions.GeckoException;
import org.gecko.exceptions.MissingViewModelElementException;
import org.gecko.exceptions.ModelException;
import org.gecko.model.Edge;
import org.gecko.model.ModelFactory;
import org.gecko.model.Region;
import org.gecko.model.State;
import org.gecko.model.System;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
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
        ModelFactory modelFactory = geckoViewModel.getGeckoModel().getModelFactory();
        if (copyVisitor.isAutomatonCopy()) {
            copyVisitor.getCopiedStates().forEach(state -> {
                State stateToPaste;
                try {
                    stateToPaste = modelFactory.copyState(state);
                } catch (ModelException e) {
                    throw new RuntimeException(e);
                }
                currentSystem.getAutomaton().addState(stateToPaste);
                geckoViewModel.addViewModelElement(factory.createStateViewModelFrom(stateToPaste));
            });
            copyVisitor.getCopiedRegions().forEach(region -> {
                Region regionToPaste;
                try {
                    regionToPaste = modelFactory.copyRegion(region);
                } catch (ModelException e) {
                    throw new RuntimeException(e);
                }
                currentSystem.getAutomaton().addRegion(regionToPaste);
                try {
                    geckoViewModel.addViewModelElement(factory.createRegionViewModelFrom(regionToPaste));
                } catch (MissingViewModelElementException e) {
                    throw new RuntimeException(e);
                }
            });
            copyVisitor.getCopiedEdges().forEach(edge -> {
                Edge edgeToPaste;
                try {
                    edgeToPaste = modelFactory.copyEdge(edge);
                } catch (ModelException e) {
                    throw new RuntimeException(e);
                }
                currentSystem.getAutomaton().addEdge(edgeToPaste);
                try {
                    geckoViewModel.addViewModelElement(factory.createEdgeViewModelFrom(edgeToPaste));
                } catch (MissingViewModelElementException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            copyVisitor.getCopiedSystems().forEach(system -> {
                System systemToPaste;
                try {
                    systemToPaste = modelFactory.copySystem(system);
                } catch (ModelException e) {
                    throw new RuntimeException(e);
                }
                systemToPaste.setParent(currentSystem);
                currentSystem.addChild(systemToPaste);
                factory.createSystemViewModelFrom(systemToPaste);
                factory.createSystemViewModelForChildren(systemToPaste);
            });
            copyVisitor.getCopiedPorts().forEach(variable -> {
                currentSystem.addVariable(variable);
                geckoViewModel.addViewModelElement(factory.createPortViewModelFrom(variable));
            });
            //pasteSystemConnectionViewModels(factory);
        }
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(pastedElements);
    }
}
