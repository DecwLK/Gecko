package org.gecko.actions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.geometry.Point2D;
import javafx.util.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.gecko.exceptions.ModelException;
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
import org.gecko.viewmodel.PositionableViewModelElement;
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
    private HashMap<Element, Element> originalToClipboard;
    private HashMap<Element, Pair<Point2D, Point2D>> elementToPosAndSize;
    @Getter
    private Set<PositionableViewModelElement<?>> failedCopies;
    private final Set<Element> copiedElements;

    public CopyPositionableViewModelElementVisitor(GeckoViewModel geckoViewModel) {
        this.geckoViewModel = geckoViewModel;
        isAutomatonCopy = geckoViewModel.getCurrentEditor().isAutomatonEditor();
        originalToClipboard = new HashMap<>();
        elementToPosAndSize = new HashMap<>();
        copiedElements = new HashSet<>();
        failedCopies = new HashSet<>();
    }

    @Override
    public Void visit(SystemViewModel systemViewModel) {
        System original = systemViewModel.getTarget();
        Pair<System, Map<Element, Element>> copyResult;
        System copy;
        try {
            copyResult = geckoViewModel.getGeckoModel().getModelFactory().copySystem(systemViewModel.getTarget());
        } catch (ModelException e) {
            failedCopies.add(systemViewModel);
            return null;
        }
        copy = copyResult.getKey();
        originalToClipboard.putAll(copyResult.getValue());
        originalToClipboard.put(original, copy);
        savePositionRecursively(original);
        copiedElements.add(copy);
        return null;
    }

    @Override
    public Void visit(RegionViewModel regionViewModel) {
        Region original = regionViewModel.getTarget();
        Region copy = geckoViewModel.getGeckoModel().getModelFactory().copyRegion(original);
        originalToClipboard.put(original, copy);
        savePositionAndSize(copy, regionViewModel);
        copiedElements.add(copy);
        return null;
    }

    @Override
    public Void visit(EdgeViewModel edgeViewModel) {
        Set<PositionableViewModelElement<?>> selection =
            geckoViewModel.getCurrentEditor().getSelectionManager().getCurrentSelection();
        if (selection.contains(edgeViewModel.getSource()) && selection.contains(edgeViewModel.getDestination())) {
            Edge original = edgeViewModel.getTarget();
            Edge copy = geckoViewModel.getGeckoModel().getModelFactory().copyEdge(original);
            State sourceOnClipboard = (State) originalToClipboard.get(original.getSource());
            State destinationOnClipboard = (State) originalToClipboard.get(original.getDestination());
            Contract contractOnClipboard = (Contract) originalToClipboard.get(original.getContract());
            if (sourceOnClipboard == null || destinationOnClipboard == null) {
                failedCopies.add(edgeViewModel);
                return null;
            }
            copy.setSource(sourceOnClipboard);
            copy.setDestination(destinationOnClipboard);
            copy.setContract(contractOnClipboard);
            originalToClipboard.put(original, copy);
            copiedElements.add(copy);
        }
        return null;
    }

    @Override
    public Void visit(StateViewModel stateViewModel) {
        State original = stateViewModel.getTarget();
        Pair<State, Map<Contract, Contract>> copyResult =
            geckoViewModel.getGeckoModel().getModelFactory().copyState(original);
        State copy = copyResult.getKey();
        originalToClipboard.putAll(copyResult.getValue());
        originalToClipboard.put(original, copy);
        savePositionAndSize(copy, stateViewModel);
        copiedElements.add(copy);
        return null;
    }

    @Override
    public Void visit(PortViewModel portViewModel) {
        return null;
    }

    @Override
    public Void visit(SystemConnectionViewModel systemConnectionViewModel) {
        Set<PositionableViewModelElement<?>> selection =
            geckoViewModel.getCurrentEditor().getSelectionManager().getCurrentSelection();
        SystemViewModel sourceSystemViewModel = (SystemViewModel) geckoViewModel.getViewModelElement(
            geckoViewModel.getCurrentEditor()
                .getCurrentSystem()
                .getTarget()
                .getChildSystemWithVariable(systemConnectionViewModel.getTarget().getSource()));
        SystemViewModel destinationSystemViewModel = (SystemViewModel) geckoViewModel.getViewModelElement(
            geckoViewModel.getCurrentEditor()
                .getCurrentSystem()
                .getTarget()
                .getChildSystemWithVariable(systemConnectionViewModel.getTarget().getDestination()));
        if (selection.contains(sourceSystemViewModel) && selection.contains(destinationSystemViewModel)) {
            SystemConnection original = systemConnectionViewModel.getTarget();
            SystemConnection copy = geckoViewModel.getGeckoModel().getModelFactory().copySystemConnection(original);
            Variable sourceOnClipboard = (Variable) originalToClipboard.get(original.getSource());
            Variable destinationOnClipboard = (Variable) originalToClipboard.get(original.getDestination());
            if (sourceOnClipboard == null || destinationOnClipboard == null) {
                failedCopies.add(systemConnectionViewModel);
                return null;
            }
            try {
                copy.setSource(sourceOnClipboard);
                copy.setDestination(destinationOnClipboard);
            } catch (ModelException e) {
                throw new RuntimeException(e);
            }
            originalToClipboard.put(original, copy);
            copiedElements.add(copy);
        }
        return null;
    }

    private void savePositionRecursively(System original) {
        System copy = (System) originalToClipboard.get(original);
        savePositionAndSize(copy, geckoViewModel.getViewModelElement(original));
        for (State state : original.getAutomaton().getStates()) {
            savePositionAndSize(originalToClipboard.get(state), geckoViewModel.getViewModelElement(state));
        }
        for (Region region : original.getAutomaton().getRegions()) {
            savePositionAndSize(originalToClipboard.get(region), geckoViewModel.getViewModelElement(region));
        }
        for (System child : original.getChildren()) {
            savePositionRecursively(child);
        }
    }

    private void savePositionAndSize(Element key, PositionableViewModelElement<?> positionSource) {
        elementToPosAndSize.put(key, new Pair<>(positionSource.getPosition(), positionSource.getSize()));
    }
}
