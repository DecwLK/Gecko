package org.gecko.actions;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.gecko.exceptions.ModelException;
import org.gecko.model.Edge;
import org.gecko.model.Region;
import org.gecko.model.State;
import org.gecko.model.System;
import org.gecko.model.SystemConnection;
import org.gecko.model.Variable;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElementVisitor;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

@Getter
@Setter
public class CopyPositionableViewModelElementVisitor implements PositionableViewModelElementVisitor {
    @Getter(AccessLevel.NONE)
    private GeckoViewModel geckoViewModel;
    private boolean isAutomatonCopy;
    private List<System> copiedSystems;
    private List<Variable> copiedPorts;
    private List<SystemConnection> copiedSystemConnections;
    private List<State> copiedStates;
    private List<Region> copiedRegions;
    private List<Edge> copiedEdges;

    public CopyPositionableViewModelElementVisitor() {
        this.geckoViewModel = null;
        this.isAutomatonCopy = false;
        copiedSystems = new ArrayList<>();
        copiedPorts = new ArrayList<>();
        copiedSystemConnections = new ArrayList<>();
        copiedStates = new ArrayList<>();
        copiedRegions = new ArrayList<>();
        copiedEdges = new ArrayList<>();
    }

    public CopyPositionableViewModelElementVisitor(GeckoViewModel geckoViewModel) {
        this.geckoViewModel = geckoViewModel;
        isAutomatonCopy = geckoViewModel.getCurrentEditor().isAutomatonEditor();
        copiedSystems = new ArrayList<>();
        copiedPorts = new ArrayList<>();
        copiedSystemConnections = new ArrayList<>();
        copiedStates = new ArrayList<>();
        copiedRegions = new ArrayList<>();
        copiedEdges = new ArrayList<>();
    }

    @Override
    public SystemViewModel visit(SystemViewModel systemViewModel) {
        try {
            copiedSystems.add(geckoViewModel.getGeckoModel().getModelFactory().copySystem(systemViewModel.getTarget()));
        } catch (ModelException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public RegionViewModel visit(RegionViewModel regionViewModel) {
        try {
            copiedRegions.add(geckoViewModel.getGeckoModel().getModelFactory().copyRegion(regionViewModel.getTarget()));
        } catch (ModelException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public SystemConnectionViewModel visit(SystemConnectionViewModel systemConnectionViewModel) {
        /*CopiedSystemConnectionWrapper systemConnectionWrapper =
            new CopiedSystemConnectionWrapper(geckoViewModel, geckoViewModel.getCurrentEditor().getCurrentSystem(),
                systemConnectionViewModel, systemConnectionViewModel.getSource(),
                systemConnectionViewModel.getDestination());
        copiedSystemConnections.add(systemConnectionWrapper);*/

        return null;
    }

    @Override
    public EdgeViewModel visit(EdgeViewModel edgeViewModel) {
        /*CopiedEdgeWrapper edgeWrapper =
            new CopiedEdgeWrapper(edgeViewModel, edgeViewModel.getSource(), edgeViewModel.getDestination());
        copiedEdges.add(edgeWrapper);*/
        return null;
    }

    @Override
    public StateViewModel visit(StateViewModel stateViewModel) {
        try {
            copiedStates.add(geckoViewModel.getGeckoModel().getModelFactory().copyState(stateViewModel.getTarget()));
        } catch (ModelException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public PortViewModel visit(PortViewModel portViewModel) {
        copiedPorts.add(portViewModel.getTarget());
        return null;
    }

    public void removeDoubleSelectedStates() {
        List<State> states = new ArrayList<>(copiedStates);

        states.forEach(state -> copiedEdges.forEach(edge -> {
            if (edge.getSource().equals(state) || edge.getDestination().equals(state)) {
                copiedStates.remove(state);
            }
        }));
    }

    public void removeDoubleSelectedSystems() {
        /*List<SystemViewModel> systems = new ArrayList<>(copiedSystems);

        systems.forEach(system -> copiedSystemConnections.forEach(systemConnection -> {
            if (systemConnection.getSourceParent().equals(system) || systemConnection.getDestinationParent()
                .equals(system)) {
                copiedSystems.remove(system);
            }
        }));*/
    }

    public boolean isWrapperEmpty() {
        return (copiedSystems == null || copiedSystems.isEmpty()) && (copiedPorts == null || copiedPorts.isEmpty()) && (
            copiedSystemConnections == null || copiedSystemConnections.isEmpty()) && (copiedStates == null
            || copiedStates.isEmpty()) && (copiedRegions == null || copiedRegions.isEmpty()) && (copiedEdges == null
            || copiedEdges.isEmpty());
    }

    /*public Set<PositionableViewModelElement<?>> getAllElements() {
        Set<PositionableViewModelElement<?>> elements = new HashSet<>();
        elements.addAll(copiedSystems);
        elements.addAll(copiedPorts);
        copiedSystemConnections.forEach(systemConnectionWrapper -> {
            elements.add(systemConnectionWrapper.getSystemConnection());
            elements.add(systemConnectionWrapper.getSourceParent());
            elements.add(systemConnectionWrapper.getDestinationParent());
        });
        elements.addAll(copiedStates);
        elements.addAll(copiedRegions);
        copiedEdges.forEach(edgeWrapper -> {
            elements.add(edgeWrapper.getEdge());
            elements.add(edgeWrapper.getSource());
            elements.add(edgeWrapper.getDestination());
        });

        return elements;
    }*/
}
