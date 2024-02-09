package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.GeckoViewModel;

public class CopyPositionableViewModelElementAction extends Action {
    GeckoViewModel geckoViewModel;
    CopyPositionableViewModelElementVisitor copyVisitor;

    CopyPositionableViewModelElementAction(
        GeckoViewModel geckoViewModel, CopyPositionableViewModelElementVisitor copyVisitor) {

        this.geckoViewModel = geckoViewModel;
        this.copyVisitor = copyVisitor;
    }

    @Override
    boolean run() throws GeckoException {
        CopyPositionableViewModelElementVisitor visitor = new CopyPositionableViewModelElementVisitor(geckoViewModel);
        geckoViewModel.getCurrentEditor().getSelectionManager().getCurrentSelection();

        geckoViewModel.getCurrentEditor()
            .getSelectionManager()
            .getCurrentSelection()
            .forEach(element -> element.accept(visitor));
        visitor.removeDoubleSelectedStates();
        visitor.removeDoubleSelectedSystems();

        copyVisitor.setAutomatonCopy(visitor.isAutomatonCopy());
        copyVisitor.setCopiedSystems(visitor.getCopiedSystems());
        copyVisitor.setCopiedPorts(visitor.getCopiedPorts());
        copyVisitor.setCopiedSystemConnections(visitor.getCopiedSystemConnections());
        copyVisitor.setCopiedStates(visitor.getCopiedStates());
        copyVisitor.setCopiedRegions(visitor.getCopiedRegions());
        copyVisitor.setCopiedEdges(visitor.getCopiedEdges());

        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
