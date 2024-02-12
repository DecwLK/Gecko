package org.gecko.actions;

import java.util.HashSet;
import java.util.Set;
import javafx.geometry.Point2D;
import org.gecko.exceptions.GeckoException;
import org.gecko.exceptions.MissingViewModelElementException;
import org.gecko.model.Contract;
import org.gecko.model.Edge;
import org.gecko.model.Region;
import org.gecko.model.State;
import org.gecko.model.System;
import org.gecko.model.Variable;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class PastePositionableViewModelElementAction extends Action {
    private final GeckoViewModel geckoViewModel;
    private final Set<PositionableViewModelElement<?>> pastedElements;
    private final Point2D pasteOffset = new Point2D(50, 50);
    private CopyPositionableViewModelElementVisitor copyVisitor;

    PastePositionableViewModelElementAction(GeckoViewModel geckoViewModel) {
        this.geckoViewModel = geckoViewModel;
        pastedElements = new HashSet<>();
    }

    @Override
    boolean run() throws GeckoException {
        copyVisitor = geckoViewModel.getActionManager().getCopyVisitor();
        if (copyVisitor == null) {
            throw new GeckoException("Invalid Clipboard. Nothing to paste.");
        }
        if (geckoViewModel.getCurrentEditor().isAutomatonEditor() != copyVisitor.isAutomatonCopy()) {
            return false;
        }

        if (copyVisitor.isAutomatonCopy()) {
            return pasteAutomaton();
        } else {
            return pasteSystem();
        }
    }

    private boolean pasteSystem() {
        for (Variable v : copyVisitor.getCopiedPorts().keySet()) {
            if (!geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget().getVariables().contains(v)) {
                continue;
            }
            Variable copy = geckoViewModel.getGeckoModel().getModelFactory().copyVariable(copyVisitor.getCopiedPorts().get(v));
            geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget().addVariable(copy);
            copyVisitor.getCopiedPorts().put(v, copy);
            PortViewModel portViewModel = geckoViewModel.getViewModelFactory().createPortViewModelFrom(copy);
            portViewModel.setPosition(copyVisitor.getCopiedPosAndSize().get(v).getKey().add(pasteOffset));
            pastedElements.add(portViewModel);
        }
        for (System s : copyVisitor.getCopiedSystems().keySet()) {
            System copy = geckoViewModel.getGeckoModel().getModelFactory().copySystem(copyVisitor.getCopiedSystems().get(s));
            geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget().addChild(copy);
            copy.getVariables().clear();
            for (Variable v : copyVisitor.getCopiedSystems().get(s).getVariables()) {
                Variable newVariable = geckoViewModel.getGeckoModel().getModelFactory().copyVariable(v);
                copy.addVariable(newVariable);
                copyVisitor.getCopiedPorts().put(v, newVariable);
            }
            copyVisitor.getCopiedSystems().put(s, copy);
            SystemViewModel systemViewModel = geckoViewModel.getViewModelFactory().createSystemViewModelFrom(copy);
            systemViewModel.setPosition(copyVisitor.getCopiedPosAndSize().get(s).getKey().add(pasteOffset));
            pastedElements.add(systemViewModel);
        }
        return false;
    }

    private boolean pasteAutomaton() throws MissingViewModelElementException {
        for (State state : copyVisitor.getCopiedStates().keySet()) {
            State copy = geckoViewModel.getGeckoModel().getModelFactory().copyState(copyVisitor.getCopiedStates().get(state));
            geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget().getAutomaton().addState(copy);
            copy.getContracts().clear();
            for (Contract c : copyVisitor.getCopiedStates().get(state).getContracts()) {
                Contract newContract = geckoViewModel.getGeckoModel().getModelFactory().copyContract(c);
                copy.addContract(newContract);
                copyVisitor.getCopiedContracts().put(c, newContract);
            }
            copyVisitor.getCopiedStates().put(state, copy);
            StateViewModel stateViewModel = geckoViewModel.getViewModelFactory().createStateViewModelFrom(copy);
            stateViewModel.setPosition(copyVisitor.getCopiedPosAndSize().get(state).getKey().add(pasteOffset));
            stateViewModel.setSize(copyVisitor.getCopiedPosAndSize().get(state).getValue());
            pastedElements.add(stateViewModel);
        }
        for (Region region : copyVisitor.getCopiedRegions().keySet()) {
            Region copy = geckoViewModel.getGeckoModel().getModelFactory().copyRegion(copyVisitor.getCopiedRegions().get(region));
            geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget().getAutomaton().addRegion(copy);
            RegionViewModel regionViewModel = geckoViewModel.getViewModelFactory().createRegionViewModelFrom(copy);
            regionViewModel.setPosition(copyVisitor.getCopiedPosAndSize().get(region).getKey().add(pasteOffset));
            regionViewModel.setSize(copyVisitor.getCopiedPosAndSize().get(region).getValue());
            pastedElements.add(regionViewModel);
        }
        for (Edge edge : copyVisitor.getCopiedEdges().keySet()) {
            Edge copy = geckoViewModel.getGeckoModel().getModelFactory().copyEdge(copyVisitor.getCopiedEdges().get(edge));
            geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget().getAutomaton().addEdge(copy);
            copy.setSource(copyVisitor.getCopiedStates().get(edge.getSource()));
            copy.setDestination(copyVisitor.getCopiedStates().get(edge.getDestination()));
            copy.setContract(copyVisitor.getCopiedContracts().get(edge.getContract()));
            EdgeViewModel edgeViewModel = geckoViewModel.getViewModelFactory().createEdgeViewModelFrom(copy);
            pastedElements.add(edgeViewModel);
        }
        return false;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(pastedElements);
    }
}
