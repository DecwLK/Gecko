package org.gecko.actions;

import java.util.HashMap;
import java.util.HashSet;
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
    private HashMap<Element, Element> originalToClipboard;
    private HashMap<Element, Pair<Point2D, Point2D>> elementToPosAndSize;
    @Getter
    private Set<PositionableViewModelElement<?>> unsuccessfulCopies;

    public CopyPositionableViewModelElementVisitor(GeckoViewModel geckoViewModel) {
        this.geckoViewModel = geckoViewModel;
        isAutomatonCopy = geckoViewModel.getCurrentEditor().isAutomatonEditor();
        originalToClipboard = new HashMap<>();
        elementToPosAndSize = new HashMap<>();
        unsuccessfulCopies = new HashSet<>();
    }

    @Override
    public Void visit(SystemViewModel systemViewModel) {
        java.lang.System.out.println("Copying system " + systemViewModel.getTarget().getName());
        System original = systemViewModel.getTarget();
        System parentOfCopy = (System) originalToClipboard.get(original.getParent());
        if (original.getParent() != geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget() && parentOfCopy == null) {
            unsuccessfulCopies.add(systemViewModel);
            return null;
        }
        System copy = geckoViewModel.getGeckoModel().getModelFactory().copySystem(systemViewModel.getTarget());
        copy.setParent(parentOfCopy);
        if (parentOfCopy != null) {
            parentOfCopy.addChild(copy);
        }
        originalToClipboard.put(original, copy);
        for (System childSystem : original.getChildren()) {
            geckoViewModel.getViewModelElement(childSystem).accept(this);
        }
        for (Variable variable : original.getVariables()) {
            Variable copyVariable = geckoViewModel.getGeckoModel().getModelFactory().copyVariable(variable);
            copy.addVariable(copyVariable);
            originalToClipboard.put(variable, copyVariable);
        }
        elementToPosAndSize.put(original,
            new Pair<>(systemViewModel.getPosition(), systemViewModel.getSize()));
        elementToPosAndSize.put(copy,
            new Pair<>(systemViewModel.getPosition(), systemViewModel.getSize()));
        return null;
    }

    @Override
    public Void visit(RegionViewModel regionViewModel) {
        originalToClipboard.put(regionViewModel.getTarget(),
            geckoViewModel.getGeckoModel().getModelFactory().copyRegion(regionViewModel.getTarget()));
        elementToPosAndSize.put(regionViewModel.getTarget(),
            new Pair<>(regionViewModel.getPosition(), regionViewModel.getSize()));
        return null;
    }

    @Override
    public Void visit(EdgeViewModel edgeViewModel) {
        Set<PositionableViewModelElement<?>> selection = geckoViewModel.getCurrentEditor().getSelectionManager().getCurrentSelection();
        if (selection.contains(edgeViewModel.getSource()) && selection.contains(edgeViewModel.getDestination())) {
            Edge original = edgeViewModel.getTarget();
            Edge copy = geckoViewModel.getGeckoModel().getModelFactory().copyEdge(edgeViewModel.getTarget());
            State sourceOnClipboard = (State) originalToClipboard.get(original.getSource());
            State destinationOnClipboard = (State) originalToClipboard.get(original.getDestination());
            if (sourceOnClipboard == null || destinationOnClipboard == null) {
                unsuccessfulCopies.add(edgeViewModel);
                return null;
            }
            copy.setSource(sourceOnClipboard);
            copy.setDestination(destinationOnClipboard);
            originalToClipboard.put(original, copy);
        }
        return null;
    }

    @Override
    public Void visit(StateViewModel stateViewModel) {
        originalToClipboard.put(stateViewModel.getTarget(),
            geckoViewModel.getGeckoModel().getModelFactory().copyState(stateViewModel.getTarget()));
        elementToPosAndSize.put(stateViewModel.getTarget(),
            new Pair<>(stateViewModel.getPosition(), stateViewModel.getSize()));
        elementToPosAndSize.put(originalToClipboard.get(stateViewModel.getTarget()),
            new Pair<>(stateViewModel.getPosition(), stateViewModel.getSize()));
        /*for (EdgeViewModel evm : stateViewModel.getOutgoingEdges()) {
            evm.accept(this);
        }*/
        return null;
    }

    @Override
    public Void visit(PortViewModel portViewModel) {
        java.lang.System.out.println("Copying port");
        Variable original = portViewModel.getTarget();
        Variable copy = geckoViewModel.getGeckoModel().getModelFactory().copyVariable(original);
        originalToClipboard.put(original, copy);

        elementToPosAndSize.put(portViewModel.getTarget(),
            new Pair<>(portViewModel.getPosition(), portViewModel.getSize()));
        return null;
    }

    @Override
    public Void visit(SystemConnectionViewModel systemConnectionViewModel) {
        Set<PositionableViewModelElement<?>> selection = geckoViewModel.getCurrentEditor().getSelectionManager().getCurrentSelection();
        SystemViewModel sourceSystemViewModel = (SystemViewModel) geckoViewModel.getViewModelElement(geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget().getChildSystemWithVariable(systemConnectionViewModel.getTarget().getSource()));
        SystemViewModel destinationSystemViewModel = (SystemViewModel) geckoViewModel.getViewModelElement(geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget().getChildSystemWithVariable(systemConnectionViewModel.getTarget().getDestination()));
        if (selection.contains(sourceSystemViewModel) && selection.contains(destinationSystemViewModel)) {
            SystemConnection original = systemConnectionViewModel.getTarget();
            SystemConnection copy = geckoViewModel.getGeckoModel().getModelFactory().copySystemConnection(original);
            Variable sourceOnClipboard = (Variable) originalToClipboard.get(original.getSource());
            Variable destinationOnClipboard = (Variable) originalToClipboard.get(original.getDestination());
            if (sourceOnClipboard == null || destinationOnClipboard == null) {
                unsuccessfulCopies.add(systemConnectionViewModel);
                return null;
            }
            try {
                copy.setSource(sourceOnClipboard);
                copy.setDestination(destinationOnClipboard);
            } catch (ModelException e) {
                throw new RuntimeException(e);
            }
            originalToClipboard.put(original, copy);
        }
        return null;
    }
}
