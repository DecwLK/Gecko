package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.exceptions.GeckoException;
import org.gecko.view.views.viewelement.decorator.ElementScalerBlock;
import org.gecko.viewmodel.BlockViewModelElement;
import org.gecko.viewmodel.EditorViewModel;

/**
 * A concrete representation of an {@link Action} that scales a  {@link BlockViewModelElement} by a
 * {@link Point2D delta value} using an {@link ElementScalerBlock}.
 */
public class ScaleBlockViewModelElementAction extends Action {

    private final EditorViewModel editorViewModel;
    private final BlockViewModelElement<?> element;
    private final ElementScalerBlock elementScalerBlock;
    private Point2D size;
    private Point2D position;
    private boolean isPreviousScale;

    ScaleBlockViewModelElementAction(
        EditorViewModel editorViewModel, BlockViewModelElement<?> element, ElementScalerBlock elementScalerBlock,
        Point2D position, Point2D size, boolean isPreviousScale) {
        this.editorViewModel = editorViewModel;
        this.element = element;
        this.elementScalerBlock = elementScalerBlock;
        this.position = position;
        this.size = size;
        this.isPreviousScale = isPreviousScale;
    }

    ScaleBlockViewModelElementAction(
        EditorViewModel editorViewModel, BlockViewModelElement<?> element, ElementScalerBlock elementScalerBlock) {
        this.editorViewModel = editorViewModel;
        this.element = element;
        this.elementScalerBlock = elementScalerBlock;
    }

    @Override
    boolean run() throws GeckoException {
        Point2D positionCopy = element.getPosition();
        Point2D sizeCopy = element.getSize();
        if (!isPreviousScale) {
            element.setSize(size);
            element.setPosition(position);
            size = sizeCopy;
            position = positionCopy;
        }

        editorViewModel.updateRegions();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createScaleBlockViewModelElementAction(element, elementScalerBlock, position, size, false);
    }
}
