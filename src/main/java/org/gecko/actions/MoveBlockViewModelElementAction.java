package org.gecko.actions;

import java.util.HashSet;
import java.util.Set;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import org.gecko.exceptions.GeckoException;
import org.gecko.exceptions.ModelException;
import org.gecko.model.Automaton;
import org.gecko.model.Region;
import org.gecko.model.State;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;

public class MoveBlockViewModelElementAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final EditorViewModel editorViewModel;
    private Set<PositionableViewModelElement<?>> elementsToMove;
    private final Point2D delta;

    MoveBlockViewModelElementAction(GeckoViewModel geckoViewModel, Point2D delta) {
        this.geckoViewModel = geckoViewModel;
        this.editorViewModel = geckoViewModel.getCurrentEditor();
        this.elementsToMove = null;
        this.delta = delta;
    }

    MoveBlockViewModelElementAction(
        GeckoViewModel geckoViewModel, Set<PositionableViewModelElement<?>> elementsToMove, Point2D delta) {
        this.geckoViewModel = geckoViewModel;
        this.editorViewModel = geckoViewModel.getCurrentEditor();
        this.elementsToMove = elementsToMove;
        this.delta = delta;
    }

    @Override
    boolean run() throws GeckoException {
        if (delta.equals(Point2D.ZERO)) {
            return false;
        }
        if (elementsToMove == null) {
            elementsToMove = new HashSet<>(editorViewModel.getSelectionManager().getCurrentSelection());
        }
        for (PositionableViewModelElement<?> element : elementsToMove) {
            element.setPosition(element.getPosition().add(delta));
        }

        geckoViewModel.updateRegions();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createMoveBlockViewModelElementAction(elementsToMove, delta.multiply(-1));
    }
}
