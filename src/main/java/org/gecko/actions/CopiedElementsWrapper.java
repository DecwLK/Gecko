package org.gecko.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Data;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

@Data
public class CopiedElementsWrapper {
    private List<PositionableViewModelElement<?>> containedSystemViewModels;

    private List<SystemViewModel> copiedSystems;
    private List<PortViewModel> copiedPorts;

    private List<CopiedSystemConnectionWrapper> copiedSystemConnections;
    private List<StateViewModel> copiedStates;
    private List<RegionViewModel> copiedRegions;
    private List<CopiedEdgeWrapper> copiedEdges;

    public CopiedElementsWrapper(Set<PositionableViewModelElement<?>> elements, EditorViewModel currentEditor) {
        this.containedSystemViewModels = currentEditor.getContainedPositionableViewModelElementsProperty()
            .stream().filter(element -> element instanceof SystemViewModel).toList();

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
            //this.copiedSystemConnections.add((SystemConnectionViewModel) systemConnectionViewModel);
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
        // copiedEdges.forEach(edgeViewModel -> this.copiedEdges.add((EdgeViewModel) edgeViewModel));
    }

    public boolean isWrapperEmpty() {
        return (copiedSystems == null || copiedSystems.isEmpty())
            && (copiedPorts == null || copiedPorts.isEmpty())
            && (copiedSystemConnections == null || copiedSystemConnections.isEmpty())
            && (copiedStates == null || copiedStates.isEmpty())
            && (copiedRegions == null || copiedRegions.isEmpty())
            && (copiedEdges == null || copiedEdges.isEmpty());
    }
}
