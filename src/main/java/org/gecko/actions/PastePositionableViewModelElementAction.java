package org.gecko.actions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;

public class PastePositionableViewModelElementAction extends Action {
    private final GeckoViewModel geckoViewModel;
    private final CopyPositionableViewModelElementVisitor copyVisitor;
    private final SystemViewModel currentSystem;
    private final Set<PositionableViewModelElement<?>> pastedElements;
    private final HashMap<StateViewModel, StateViewModel> states;
    private final HashMap<SystemViewModel, SystemViewModel> systems;
    private static final Point2D POSITION_OFFSET = new Point2D(20, 20);

    PastePositionableViewModelElementAction(
        GeckoViewModel geckoViewModel, CopyPositionableViewModelElementVisitor copyVisitor) {
        this.geckoViewModel = geckoViewModel;
        this.currentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem();
        this.copyVisitor = copyVisitor;
        pastedElements = new HashSet<>();
        states = new HashMap<>();
        systems = new HashMap<>();
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
        if (copyVisitor.isAutomatonCopy()) {
            pasteStateViewModels(factory);
            pasteRegionViewModels(factory);
            pasteEdgeViewModels(factory);
        } else {
            pasteSystemViewModels(factory);
            pastePortViewModels(factory);
            pasteSystemConnectionViewModels(factory);
        }
        return true;
    }

    private void pasteStateViewModels(ViewModelFactory factory) throws GeckoException {
        for (StateViewModel copiedState : copyVisitor.getCopiedStates()) {
            pasteStateViewModel(factory, copiedState);
        }
    }

    private void pasteStateViewModel(ViewModelFactory factory, StateViewModel copiedState) throws GeckoException {
        if (states.get(copiedState) != null) {
            return;
        }

        StateViewModel newState = factory.createStateViewModelIn(currentSystem);
        newState.setName(copiedState.getName());
        newState.setStartState(copiedState.getIsStartState());
        newState.setPosition(copiedState.getPosition().add(POSITION_OFFSET));
        newState.setSize(copiedState.getSize());
        newState.updateTarget();
        states.put(copiedState, newState);
        pastedElements.add(newState);
    }

    private void pasteRegionViewModels(ViewModelFactory factory) throws GeckoException {
        for (RegionViewModel copiedRegion : copyVisitor.getCopiedRegions()) {
            RegionViewModel newRegion = factory.createRegionViewModelIn(currentSystem);
            // TODO: what happens when state is selected with region at copy? -> update state list in region?
            newRegion.setName(copiedRegion.getName());
            newRegion.setInvariant(copiedRegion.getInvariant());
            newRegion.setColor(copiedRegion.getColor());
            newRegion.setPosition(copiedRegion.getPosition().add(POSITION_OFFSET));
            newRegion.setSize(copiedRegion.getSize());
            newRegion.updateTarget();
            pastedElements.add(newRegion);
        }
    }

    private void pasteEdgeViewModels(ViewModelFactory factory) throws GeckoException {
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
    }

    private void pasteSystemViewModels(ViewModelFactory factory) throws GeckoException {
        for (SystemViewModel copiedSystem : copyVisitor.getCopiedSystems()) {
            pasteSystemViewModel(factory, copiedSystem);
        }
    }

    private SystemViewModel pasteSystemViewModel(ViewModelFactory factory, SystemViewModel copiedSystem)
        throws GeckoException {
        if (systems.get(copiedSystem) != null) {
            return systems.get(copiedSystem);
        }

        SystemViewModel newSystem = factory.createSystemViewModelIn(currentSystem);
        newSystem.setName(copiedSystem.getName());

        if (copiedSystem.getCode() != null) {
            newSystem.setCode(copiedSystem.getCode());
        }

        for (PortViewModel portViewModel : newSystem.getPorts()) {
            PortViewModel port = pastePortViewModel(factory, portViewModel);
            newSystem.addPort(port);
        }

        newSystem.setPosition(copiedSystem.getPosition().add(POSITION_OFFSET));
        newSystem.setSize(copiedSystem.getSize());
        newSystem.updateTarget();

        systems.put(copiedSystem, newSystem);
        pastedElements.add(newSystem);
        return newSystem;
    }

    private void pastePortViewModels(ViewModelFactory factory) throws GeckoException {
        for (PortViewModel copiedPort : copyVisitor.getCopiedPorts()) {
            PortViewModel port = pastePortViewModel(factory, copiedPort);
            pastedElements.add(pastePortViewModel(factory, port));
        }
    }

    private PortViewModel pastePortViewModel(ViewModelFactory factory, PortViewModel copiedPort) throws GeckoException {
        PortViewModel newPort = factory.createPortViewModelIn(currentSystem);

        newPort.setName(copiedPort.getName());
        newPort.setVisibility(copiedPort.getVisibility());
        newPort.setType(copiedPort.getType());
        newPort.setPosition(copiedPort.getPosition().add(POSITION_OFFSET));
        newPort.setSize(copiedPort.getSize());
        newPort.updateTarget();

        return newPort;
    }

    private void pasteSystemConnectionViewModels(ViewModelFactory factory) throws GeckoException {
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
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(pastedElements);
    }
}
