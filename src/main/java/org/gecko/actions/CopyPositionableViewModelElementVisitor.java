package org.gecko.actions;

import java.util.HashMap;
import javafx.geometry.Point2D;
import javafx.util.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Contract;
import org.gecko.model.Edge;
import org.gecko.model.Element;
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
public class CopyPositionableViewModelElementVisitor implements PositionableViewModelElementVisitor<Void> {
    @Getter(AccessLevel.NONE)
    private GeckoViewModel geckoViewModel;
    private boolean isAutomatonCopy;
    private HashMap<State, State> copiedStates;
    private HashMap<System, System> copiedSystems;
    private HashMap<Region, Region> copiedRegions;
    private HashMap<Edge, Edge> copiedEdges;
    private HashMap<SystemConnection, SystemConnection> copiedSystemConnections;
    private HashMap<Variable, Variable> copiedPorts;
    private HashMap<Contract, Contract> copiedContracts;
    private HashMap<Element, Pair<Point2D, Point2D>> copiedPosAndSize;

    public CopyPositionableViewModelElementVisitor(GeckoViewModel geckoViewModel) {
        this.geckoViewModel = geckoViewModel;
        isAutomatonCopy = geckoViewModel.getCurrentEditor().isAutomatonEditor();
        copiedStates = new HashMap<>();
        copiedSystems = new HashMap<>();
        copiedRegions = new HashMap<>();
        copiedEdges = new HashMap<>();
        copiedSystemConnections = new HashMap<>();
        copiedPorts = new HashMap<>();
        copiedContracts = new HashMap<>();
        copiedPosAndSize = new HashMap<>();
    }

    @Override
    public Void visit(SystemViewModel systemViewModel) {
        copiedSystems.put(systemViewModel.getTarget(),
            geckoViewModel.getGeckoModel().getModelFactory().copySystem(systemViewModel.getTarget()));
        copiedPosAndSize.put(systemViewModel.getTarget(),
            new Pair<>(systemViewModel.getPosition(), systemViewModel.getSize()));
        for (PortViewModel p : systemViewModel.getPorts()) {
            if (copiedPorts.get(p.getTarget()) == null) {
                continue;
            }
            copiedPorts.put(p.getTarget(),
                geckoViewModel.getGeckoModel().getModelFactory().copyVariable(p.getTarget()));
            copiedPosAndSize.put(p.getTarget(), new Pair<>(p.getPosition(), p.getSize()));
        }
        return null;
    }

    @Override
    public Void visit(RegionViewModel regionViewModel) {
        copiedRegions.put(regionViewModel.getTarget(),
            geckoViewModel.getGeckoModel().getModelFactory().copyRegion(regionViewModel.getTarget()));
        copiedPosAndSize.put(regionViewModel.getTarget(),
            new Pair<>(regionViewModel.getPosition(), regionViewModel.getSize()));
        return null;
    }

    @Override
    public Void visit(EdgeViewModel edgeViewModel) {
        var selection = geckoViewModel.getCurrentEditor().getSelectionManager().getCurrentSelection();
        if (selection.contains(edgeViewModel.getSource()) && selection.contains(edgeViewModel.getDestination())) {
            copiedEdges.put(edgeViewModel.getTarget(),
                geckoViewModel.getGeckoModel().getModelFactory().copyEdge(edgeViewModel.getTarget()));
        }
        return null;
    }

    @Override
    public Void visit(StateViewModel stateViewModel) {
        copiedStates.put(stateViewModel.getTarget(),
            geckoViewModel.getGeckoModel().getModelFactory().copyState(stateViewModel.getTarget()));
        copiedPosAndSize.put(stateViewModel.getTarget(),
            new Pair<>(stateViewModel.getPosition(), stateViewModel.getSize()));
        for (Contract c : stateViewModel.getTarget().getContracts()) {
            copiedContracts.put(c, geckoViewModel.getGeckoModel().getModelFactory().copyContract(c));
        }
        for (EdgeViewModel evm : stateViewModel.getOutgoingEdges()) {
            evm.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(PortViewModel portViewModel) {
        copiedPorts.put(portViewModel.getTarget(),
            geckoViewModel.getGeckoModel().getModelFactory().copyVariable(portViewModel.getTarget()));
        copiedPosAndSize.put(portViewModel.getTarget(),
            new Pair<>(portViewModel.getPosition(), portViewModel.getSize()));
        return null;
    }

    @Override
    public Void visit(SystemConnectionViewModel systemConnectionViewModel) {
        return null;
    }
}
