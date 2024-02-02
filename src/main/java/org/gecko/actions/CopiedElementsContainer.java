package org.gecko.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

@Data
public class CopiedElementsContainer {
    private boolean isAutomatonCopy;
    private List<PositionableViewModelElement<?>> containedSystemViewModels;
    private List<SystemViewModel> copiedSystems;
    private List<PortViewModel> copiedPorts;
    private List<CopiedSystemConnectionWrapper> copiedSystemConnections;
    private List<StateViewModel> copiedStates;
    private List<RegionViewModel> copiedRegions;
    private List<CopiedEdgeWrapper> copiedEdges;

    public CopiedElementsContainer() {
        containedSystemViewModels = new ArrayList<>();
        copiedSystems = new ArrayList<>();
        copiedPorts = new ArrayList<>();
        copiedSystemConnections = new ArrayList<>();
        copiedStates = new ArrayList<>();
        copiedRegions = new ArrayList<>();
        copiedEdges = new ArrayList<>();
    }

    public CopiedElementsContainer(
        boolean isAutomatonCopy, List<PositionableViewModelElement<?>> containedSystemViewModels,
        Set<PositionableViewModelElement<?>> elements) {
        this.isAutomatonCopy = isAutomatonCopy;
        this.containedSystemViewModels = containedSystemViewModels;

        this.copiedSystems = new ArrayList<>();
        this.copiedPorts = new ArrayList<>();
        this.copiedSystemConnections = new ArrayList<>();

        this.copiedStates = new ArrayList<>();
        this.copiedRegions = new ArrayList<>();
        this.copiedEdges = new ArrayList<>();

        List<PositionableViewModelElement<?>> copiedSystems
            = elements.stream().filter(element -> element instanceof SystemViewModel).toList();
        copiedSystems.forEach(systemViewModel
            -> this.copiedSystems.add((SystemViewModel) systemViewModel));

        List<PositionableViewModelElement<?>> copiedPorts
            = elements.stream().filter(element -> element instanceof PortViewModel).toList();
        copiedPorts.forEach(portViewModel
            -> this.copiedPorts.add((PortViewModel) portViewModel));

        List<PositionableViewModelElement<?>> copiedSystemConnections
            = elements.stream().filter(element -> element instanceof SystemConnectionViewModel).toList();
        copiedSystemConnections.forEach(systemConnectionViewModel -> {
            CopiedSystemConnectionWrapper systemConnectionWrapper
                = new CopiedSystemConnectionWrapper((SystemConnectionViewModel) systemConnectionViewModel,
                ((SystemConnectionViewModel) systemConnectionViewModel).getSource(),
                ((SystemConnectionViewModel) systemConnectionViewModel).getDestination());
            this.copiedSystemConnections.add(systemConnectionWrapper);
        });

        List<PositionableViewModelElement<?>> copiedStates
            = elements.stream().filter(element -> element instanceof StateViewModel).toList();
        copiedStates.forEach(stateViewModel
            -> this.copiedStates.add((StateViewModel) stateViewModel));

        List<PositionableViewModelElement<?>> copiedRegions
            = elements.stream().filter(element -> element instanceof RegionViewModel).toList();
        copiedRegions.forEach(regionViewModel
            -> this.copiedRegions.add((RegionViewModel) regionViewModel));

        List<PositionableViewModelElement<?>> copiedEdges
            = elements.stream().filter(element -> element instanceof EdgeViewModel).toList();
        copiedEdges.forEach(edgeViewModel -> {
            CopiedEdgeWrapper edgeWrapper = new CopiedEdgeWrapper((EdgeViewModel) edgeViewModel,
                ((EdgeViewModel) edgeViewModel).getSource(), ((EdgeViewModel) edgeViewModel).getDestination());
            this.copiedEdges.add(edgeWrapper);
        });

        removeDoubleSelectedStates();
    }

    private void removeDoubleSelectedStates() {
        List<StateViewModel> states = new ArrayList<>(copiedStates);

        states.forEach(state -> copiedEdges.forEach(edge -> {
            if (edge.getSource().equals(state) || edge.getDestination().equals(state)) {
                copiedStates.remove(state);
            }
        }));
    }

    public boolean isWrapperEmpty() {
        return (copiedSystems == null || copiedSystems.isEmpty())
            && (copiedPorts == null || copiedPorts.isEmpty())
            && (copiedSystemConnections == null || copiedSystemConnections.isEmpty())
            && (copiedStates == null || copiedStates.isEmpty())
            && (copiedRegions == null || copiedRegions.isEmpty())
            && (copiedEdges == null || copiedEdges.isEmpty());
    }

    public Set<PositionableViewModelElement<?>> getAllElements() {
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
    }
}
