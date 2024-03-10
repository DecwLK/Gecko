package org.gecko.actions;

import java.util.HashSet;
import java.util.Set;
import javafx.geometry.Point2D;
import org.gecko.exceptions.GeckoException;
import org.gecko.model.Element;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class PastePositionableViewModelElementAction extends Action {
    private final GeckoViewModel geckoViewModel;
    private final Set<PositionableViewModelElement<?>> pastedElements;
    private final Point2D pasteOffset;

    PastePositionableViewModelElementAction(GeckoViewModel geckoViewModel, Point2D center) {
        this.geckoViewModel = geckoViewModel;
        this.pastedElements = new HashSet<>();
        this.pasteOffset = center;
    }

    @Override
    boolean run() throws GeckoException {
        CopyPositionableViewModelElementVisitor copyVisitor = geckoViewModel.getActionManager().getCopyVisitor();
        if (copyVisitor == null) {
            throw new GeckoException("Invalid Clipboard. Nothing to paste.");
        }
        if (geckoViewModel.getCurrentEditor().isAutomatonEditor() != copyVisitor.isAutomatonCopy()) {
            return false;
        }

        PastePositionableViewModelElementVisitor pasteVisitor =
            new PastePositionableViewModelElementVisitor(geckoViewModel, copyVisitor, pasteOffset);
        for (Element element : copyVisitor.getCopiedElements()) {
            element.accept(pasteVisitor);
        }
        while (!pasteVisitor.getUnsuccessfulPastes().isEmpty()) {
            Set<Element> unsuccessfulPastes = new HashSet<>(pasteVisitor.getUnsuccessfulPastes());
            pasteVisitor.getUnsuccessfulPastes().clear();
            for (Element element : unsuccessfulPastes) {
                element.accept(pasteVisitor);
            }
        }
        pasteVisitor.updatePositions();
        pastedElements.addAll(pasteVisitor.getPastedElements());
        /*Action selectAction =
            geckoViewModel.getActionManager().getActionFactory().createSelectAction(pastedElements, true);
        geckoViewModel.getActionManager().run(selectAction);*/
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeletePositionableViewModelElementAction(pastedElements);
    }
}
