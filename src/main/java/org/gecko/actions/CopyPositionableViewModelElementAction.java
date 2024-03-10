package org.gecko.actions;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.gecko.exceptions.GeckoException;
import org.gecko.model.Edge;
import org.gecko.model.Element;
import org.gecko.model.System;
import org.gecko.model.SystemConnection;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class CopyPositionableViewModelElementAction extends Action {
    GeckoViewModel geckoViewModel;

    CopyPositionableViewModelElementAction(GeckoViewModel geckoViewModel) {
        this.geckoViewModel = geckoViewModel;
    }

    @Override
    boolean run() throws GeckoException {
        CopyPositionableViewModelElementVisitor visitor = new CopyPositionableViewModelElementVisitor(geckoViewModel);

        Set<PositionableViewModelElement<?>> copyQueue = geckoViewModel.getCurrentEditor().getSelectionManager().getCurrentSelection();
        Set<Element> elementToCopy = copyQueue.stream().map(PositionableViewModelElement::getTarget).collect(Collectors.toSet());
        for (Edge edge : geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget().getAutomaton().getEdges()) {
            if (elementToCopy.contains(edge.getSource()) && elementToCopy.contains(edge.getDestination())) {
                copyQueue.add(geckoViewModel.getViewModelElement(edge));
            }
        }
        for (SystemConnection connection : geckoViewModel.getCurrentEditor().getCurrentSystem().getTarget().getConnections()) {
            System sourceSystem = geckoViewModel.getGeckoModel().getSystemWithVariable(connection.getSource());
            System destinationSystem = geckoViewModel.getGeckoModel().getSystemWithVariable(connection.getDestination());
            if (elementToCopy.contains(sourceSystem) && elementToCopy.contains(destinationSystem)) {
                copyQueue.add(geckoViewModel.getViewModelElement(connection));
            }
        }
        do {
            visitor.getFailedCopies().clear();
            copyQueue.forEach(element -> element.accept(visitor));
            copyQueue = new HashSet<>(visitor.getFailedCopies());
        } while (!copyQueue.isEmpty());

        geckoViewModel.getActionManager().setCopyVisitor(visitor);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
