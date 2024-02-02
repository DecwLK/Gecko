package org.gecko.actions;

import java.util.HashSet;
import java.util.List;
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
    private final CopiedElementsContainer elementsToPaste;
    private final SystemViewModel currentSystem;
    private final Set<PositionableViewModelElement<?>> pastedElements;
    private static final Point2D POSITION_OFFSET = new Point2D(20, 20);

    PastePositionableViewModelElementAction(GeckoViewModel geckoViewModel, CopiedElementsContainer elementsToPaste) {
        this.geckoViewModel = geckoViewModel;
        this.currentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem();
        this.elementsToPaste = elementsToPaste;
        pastedElements = new HashSet<>();
    }

    /*PastePositionableViewModelElementAction(
        GeckoViewModel geckoViewModel, List<PositionableViewModelElement<?>> elements) {
        this.geckoViewModel = geckoViewModel;
        this.currentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem();
        elementsToPaste = null;
    }*/

    @Override
    boolean run() throws GeckoException {
        if (elementsToPaste != null) {
            if (elementsToPaste.isWrapperEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Clipboard is empty, so there is nothing to paste.",
                    ButtonType.OK);
                alert.showAndWait();
            } else {
                if (geckoViewModel.getCurrentEditor().isAutomatonEditor() != elementsToPaste.isAutomatonCopy()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Cannot paste here, because copied content " + "is incompatible with opened view.",
                        ButtonType.OK);
                    alert.showAndWait();
                } else {
                    ViewModelFactory factory = geckoViewModel.getViewModelFactory();

                    if (elementsToPaste.isAutomatonCopy()) {
                        pasteStateViewModels(factory);
                        pasteRegionViewModels(factory);
                        pasteEdgeViewModels(factory);
                    } else {
                        pasteSystemViewModels(factory);
                        pastePortViewModels(factory);
                        pasteSystemConnectionViewModels(factory);
                    }
                }
            }
        }
        return true;
    }

    private void pasteStateViewModels(ViewModelFactory factory) throws GeckoException {
        for (StateViewModel copiedState : elementsToPaste.getCopiedStates()) {
            pastedElements.add(pasteStateViewModel(factory, copiedState));
        }
    }

    private StateViewModel pasteStateViewModel(ViewModelFactory factory, StateViewModel copiedState)
        throws GeckoException {
        StateViewModel newState = factory.createStateViewModelIn(currentSystem);
        newState.setName(copiedState.getName());
        newState.setStartState(copiedState.getIsStartState());
        newState.setPosition(copiedState.getPosition().add(POSITION_OFFSET));
        newState.setSize(copiedState.getSize());
        newState.updateTarget();

        return newState;
    }

    private void pasteRegionViewModels(ViewModelFactory factory) throws GeckoException {
        for (RegionViewModel copiedRegion : elementsToPaste.getCopiedRegions()) {
            RegionViewModel newRegion = factory.createRegionViewModelIn(currentSystem);
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
        for (CopiedEdgeWrapper copiedEdgeWrapper : elementsToPaste.getCopiedEdges()) {
            StateViewModel newSource = pasteStateViewModel(factory, copiedEdgeWrapper.getSource());
            StateViewModel newDestination = pasteStateViewModel(factory, copiedEdgeWrapper.getDestination());

            EdgeViewModel newEdge = factory.createEdgeViewModelIn(currentSystem, newSource, newDestination);
            if (copiedEdgeWrapper.getEdge().getContract() != null) {
                newEdge.setContract(copiedEdgeWrapper.getEdge().getContract());
            }
            newEdge.setKind(copiedEdgeWrapper.getEdge().getKind());
            newEdge.setPriority(copiedEdgeWrapper.getEdge().getPriority());
            newEdge.setPosition(copiedEdgeWrapper.getEdge().getPosition().add(POSITION_OFFSET));
            newEdge.setSize(copiedEdgeWrapper.getEdge().getSize());
            newEdge.updateTarget();

            pastedElements.add(newSource);
            pastedElements.add(newDestination);
            pastedElements.add(newEdge);
        }
    }

    private void pasteSystemViewModels(ViewModelFactory factory) throws GeckoException {
        for (SystemViewModel copiedSystem : elementsToPaste.getCopiedSystems()) {
            pastedElements.add(pasteSystemViewModel(factory, copiedSystem));
        }
    }

    private SystemViewModel pasteSystemViewModel(ViewModelFactory factory, SystemViewModel copiedSystem)
        throws GeckoException {
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

        return newSystem;
    }

    private void pastePortViewModels(ViewModelFactory factory) throws GeckoException {
        for (PortViewModel copiedPort : elementsToPaste.getCopiedPorts()) {
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
        for (CopiedSystemConnectionWrapper copiedSystemConnection : elementsToPaste.getCopiedSystemConnections()) {
            PortViewModel copiedSource = copiedSystemConnection.getSource();
            PortViewModel copiedDestination = copiedSystemConnection.getDestination();

            PortViewModel newSource = pastePortViewModel(factory, copiedSource);
            PortViewModel newDestination = pastePortViewModel(factory, copiedDestination);

            SystemViewModel copiedSourceParent = null;
            SystemViewModel copiedDestinationParent = null;

            List<PositionableViewModelElement<?>> children
                = geckoViewModel.getCurrentEditor().getContainedPositionableViewModelElementsProperty()
                .stream().filter(element -> element instanceof SystemViewModel).toList();

            for (PositionableViewModelElement<?> child : children) {
                List<PortViewModel> ports = ((SystemViewModel) child).getPorts();
                for (PortViewModel port : ports) {
                    if (port.equals(copiedSystemConnection.getSource())) {
                        copiedSourceParent = (SystemViewModel) child;
                        copiedSourceParent.getPorts().remove(copiedSource);
                    } else if (port.equals(copiedSystemConnection.getDestination())) {
                        copiedDestinationParent = (SystemViewModel) child;
                        copiedDestinationParent.getPorts().remove(copiedDestination);
                    }
                }
            }

            if (copiedSourceParent != null && copiedDestinationParent != null) {
                SystemViewModel newSourceParent = pasteSystemViewModel(factory, copiedSourceParent);
                newSourceParent.getPorts().add(newSource);

                SystemViewModel newDestinationParent = pasteSystemViewModel(factory, copiedDestinationParent);
                newDestinationParent.getPorts().add(newDestination);

                copiedSourceParent.getPorts().add(copiedSource);
                copiedDestinationParent.getPorts().add(copiedDestination);
            }

            SystemConnectionViewModel newSystemConnection
                = factory.createSystemConnectionViewModelIn(currentSystem, newSource, newDestination);
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
