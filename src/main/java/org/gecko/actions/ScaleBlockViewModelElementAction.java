package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.exceptions.GeckoException;
import org.gecko.view.views.viewelement.decorator.ElementScalerBlock;
import org.gecko.viewmodel.BlockViewModelElement;
import org.gecko.viewmodel.EditorViewModel;

public class ScaleBlockViewModelElementAction extends Action {

    private final EditorViewModel editorViewModel;
    private final BlockViewModelElement<?> element;
    private final ElementScalerBlock elementScalerBlock;
    private final Point2D delta;

    ScaleBlockViewModelElementAction(
        EditorViewModel editorViewModel, BlockViewModelElement<?> element, ElementScalerBlock elementScalerBlock,
        Point2D delta) {
        this.editorViewModel = editorViewModel;
        this.element = element;
        this.elementScalerBlock = elementScalerBlock;
        this.delta = delta;
    }

    @Override
    boolean run() throws GeckoException {
        if (delta.equals(Point2D.ZERO)) {
            return false;
        }

        elementScalerBlock.setPoint(elementScalerBlock.getPoint().add(delta));
        editorViewModel.updateRegions();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createScaleBlockViewModelElementAction(element, elementScalerBlock, delta.multiply(-1));
    }
}
