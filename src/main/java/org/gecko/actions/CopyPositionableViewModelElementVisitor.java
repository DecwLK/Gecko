package org.gecko.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
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
import org.gecko.viewmodel.PositionableViewModelElement;
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
    private HashMap<State, State> stateToCopyMap = new HashMap<>();

    public CopyPositionableViewModelElementVisitor() {
        this.geckoViewModel = null;
        this.isAutomatonCopy = false;
        copiedSystems = new ArrayList<>();
        copiedPorts = new ArrayList<>();
        copiedSystemConnections = new ArrayList<>();
        copiedStates = new ArrayList<>();
        copiedRegions = new ArrayList<>();
        copiedEdges = new ArrayList<>();
        stateToCopyMap = new HashMap<>();
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
        try {
            Edge copy = geckoViewModel.getGeckoModel().getModelFactory().copyEdge(edgeViewModel.getTarget());
            if (stateToCopyMap.get(edgeViewModel.getSource().getTarget()) == null) {
                visit(edgeViewModel.getSource());
                java.lang.System.out.println("source was null");
            }
            if (stateToCopyMap.get(edgeViewModel.getDestination().getTarget()) == null) {
                visit(edgeViewModel.getDestination());
                java.lang.System.out.println("destination was null");
            }
            copy.setSource(stateToCopyMap.get(edgeViewModel.getSource().getTarget()));
            copy.setDestination(stateToCopyMap.get(edgeViewModel.getDestination().getTarget()));
            geckoViewModel.getViewModelFactory().createStateViewModelFrom(copy.getSource());
            geckoViewModel.getViewModelFactory().createStateViewModelFrom(copy.getDestination());
            copiedEdges.add(copy);
        } catch (ModelException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public StateViewModel visit(StateViewModel stateViewModel) {
        try {
            State copy = geckoViewModel.getGeckoModel().getModelFactory().copyState(stateViewModel.getTarget());
            copiedStates.add(copy);
            stateToCopyMap.put(stateViewModel.getTarget(), copy);
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
