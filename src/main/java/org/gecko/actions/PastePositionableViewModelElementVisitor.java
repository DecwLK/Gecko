package org.gecko.actions;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.geometry.Point2D;
import javafx.util.Pair;
import lombok.Getter;
import org.gecko.exceptions.MissingViewModelElementException;
import org.gecko.exceptions.ModelException;
import org.gecko.model.Contract;
import org.gecko.model.Edge;
import org.gecko.model.Element;
import org.gecko.model.ElementVisitor;
import org.gecko.model.Region;
import org.gecko.model.State;
import org.gecko.model.System;
import org.gecko.model.SystemConnection;
import org.gecko.model.Variable;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class PastePositionableViewModelElementVisitor implements ElementVisitor {
    private final GeckoViewModel geckoViewModel;
    private final CopyPositionableViewModelElementVisitor copyVisitor;
    private final Point2D pasteOffset = new Point2D(50, 50);
    @Getter
    private final Set<PositionableViewModelElement<?>> pastedElements;
    private final BiMap<Element, Element> clipboardToPasted;
    @Getter
    private final Set<Element> unsuccessfulPastes;

    PastePositionableViewModelElementVisitor(
        GeckoViewModel geckoViewModel, CopyPositionableViewModelElementVisitor copyVisitor) {
        this.geckoViewModel = geckoViewModel;
        this.copyVisitor = copyVisitor;
        pastedElements = new HashSet<>();
        clipboardToPasted = HashBiMap.create();
        unsuccessfulPastes = new HashSet<>();
    }

    @Override
    public void visit(State stateFromClipboard) throws ModelException {
        Pair<State, Map<Contract, Contract>> copyResult =
            geckoViewModel.getGeckoModel().getModelFactory().copyState(stateFromClipboard);
        State stateToPaste = copyResult.getKey();
        clipboardToPasted.putAll(copyResult.getValue());
        geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget().getAutomaton().addState(stateToPaste);
        StateViewModel stateViewModel = geckoViewModel.getViewModelFactory().createStateViewModelFrom(stateToPaste);
        stateViewModel.setPosition(
            copyVisitor.getElementToPosAndSize().get(stateFromClipboard).getKey().add(pasteOffset));
        stateViewModel.setSize(copyVisitor.getElementToPosAndSize().get(stateFromClipboard).getValue());
        clipboardToPasted.put(stateFromClipboard, stateToPaste);
        pastedElements.add(stateViewModel);
    }

    @Override
    public void visit(Contract contract) {
    }

    @Override
    public void visit(SystemConnection connectionFromClipboard) throws MissingViewModelElementException {
        SystemConnection connectionToPaste =
            geckoViewModel.getGeckoModel().getModelFactory().copySystemConnection(connectionFromClipboard);
        Variable pastedSource = (Variable) clipboardToPasted.get(connectionFromClipboard.getSource());
        Variable pastedDestination = (Variable) clipboardToPasted.get(connectionFromClipboard.getDestination());
        if (pastedSource == null || pastedDestination == null) {
            unsuccessfulPastes.add(connectionFromClipboard);
            return;
        }
        try {
            connectionToPaste.setSource(pastedSource);
            connectionToPaste.setDestination(pastedDestination);
        } catch (ModelException e) {
            throw new RuntimeException(e);
        }
        geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget().addConnection(connectionToPaste);
        SystemConnectionViewModel systemConnectionViewModel =
            geckoViewModel.getViewModelFactory().createSystemConnectionViewModelFrom(connectionToPaste);
        pastedElements.add(systemConnectionViewModel);
    }

    @Override
    public void visit(Variable variableFromClipboard) throws ModelException {
        if (!geckoViewModel.getCurrentEditor()
            .getCurrentSystem()
            .getTarget()
            .getVariables()
            .contains(variableFromClipboard)) {
            return;
        }
        Variable variableToPaste = geckoViewModel.getGeckoModel().getModelFactory().copyVariable(variableFromClipboard);
        geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget().addVariable(variableToPaste);
        PortViewModel portViewModel = geckoViewModel.getViewModelFactory().createPortViewModelFrom(variableToPaste);
        //portViewModel.setPosition(copyVisitor.getElementToPosAndSize().get(variableFromClipboard).getKey().add(pasteOffset));
        clipboardToPasted.put(variableFromClipboard, variableToPaste);
        pastedElements.add(portViewModel);
    }

    @Override
    public void visit(System systemFromClipboard) throws ModelException {
        if (systemFromClipboard.getParent() != null) {
            return;
        }
        Pair<System, Map<Element, Element>> copyResult =
            geckoViewModel.getGeckoModel().getModelFactory().copySystem(systemFromClipboard);
        System systemToPaste = copyResult.getKey();
        clipboardToPasted.putAll(copyResult.getValue());
        geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget().addChild(systemToPaste);
        systemToPaste.setParent(geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget());
        createRecursiveSystemViewModels(systemToPaste);
        clipboardToPasted.put(systemFromClipboard, systemToPaste);
    }

    private void createRecursiveSystemViewModels(System systemToPaste) {
        System systemFromClipboard = (System) clipboardToPasted.inverse().get(systemToPaste);
        SystemViewModel systemViewModel = geckoViewModel.getViewModelFactory().createSystemViewModelFrom(systemToPaste);
        systemViewModel.setPosition(copyVisitor.getElementToPosAndSize().get(systemFromClipboard).getKey());
        pastedElements.add(systemViewModel);
        for (Variable variable : systemToPaste.getVariables()) {
            geckoViewModel.getViewModelFactory().createPortViewModelFrom(variable);
        }
        for (State state : systemToPaste.getAutomaton().getStates()) {
            StateViewModel stateViewModel = geckoViewModel.getViewModelFactory().createStateViewModelFrom(state);
            stateViewModel.setPosition(copyVisitor.getElementToPosAndSize()
                .get(clipboardToPasted.inverse().get(state))
                .getKey()
                .add(pasteOffset));
        }
        for (Region region : systemToPaste.getAutomaton().getRegions()) {
            try {
                RegionViewModel regionViewModel =
                    geckoViewModel.getViewModelFactory().createRegionViewModelFrom(region);
                regionViewModel.setPosition(copyVisitor.getElementToPosAndSize()
                    .get(clipboardToPasted.inverse().get(region))
                    .getKey()
                    .add(pasteOffset));
            } catch (MissingViewModelElementException e) {
                throw new RuntimeException(e);
            }
        }
        for (Edge edge : systemToPaste.getAutomaton().getEdges()) {
            try {
                geckoViewModel.getViewModelFactory().createEdgeViewModelFrom(edge);
            } catch (MissingViewModelElementException e) {
                throw new RuntimeException(e);
            }
        }
        for (System child : systemToPaste.getChildren()) {
            createRecursiveSystemViewModels(child);
        }
        for (SystemConnection connection : systemToPaste.getConnections()) {
            try {
                geckoViewModel.getViewModelFactory().createSystemConnectionViewModelFrom(connection);
            } catch (MissingViewModelElementException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void visit(Region regionFromClipboard) throws ModelException, MissingViewModelElementException {
        Region regionToPaste = geckoViewModel.getGeckoModel().getModelFactory().copyRegion(regionFromClipboard);
        clipboardToPasted.put(regionFromClipboard, regionToPaste);
        geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget().getAutomaton().addRegion(regionToPaste);
        RegionViewModel regionViewModel = geckoViewModel.getViewModelFactory().createRegionViewModelFrom(regionToPaste);
        regionViewModel.setPosition(
            copyVisitor.getElementToPosAndSize().get(regionFromClipboard).getKey().add(pasteOffset));
        regionViewModel.setSize(copyVisitor.getElementToPosAndSize().get(regionFromClipboard).getValue());
        pastedElements.add(regionViewModel);
    }

    @Override
    public void visit(Edge edge) throws ModelException, MissingViewModelElementException {
        Edge copy = geckoViewModel.getGeckoModel().getModelFactory().copyEdge(edge);
        State pastedSource = (State) clipboardToPasted.get(edge.getSource());
        State pastedDestination = (State) clipboardToPasted.get(edge.getDestination());
        Contract pastedContract = (Contract) clipboardToPasted.get(edge.getContract());
        if (pastedSource == null || pastedDestination == null) {
            unsuccessfulPastes.add(edge);
            return;
        }
        geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget().getAutomaton().addEdge(copy);
        copy.setSource(pastedSource);
        copy.setDestination(pastedDestination);
        copy.setContract(pastedContract);
        EdgeViewModel edgeViewModel = geckoViewModel.getViewModelFactory().createEdgeViewModelFrom(copy);
        pastedElements.add(edgeViewModel);
    }
}
