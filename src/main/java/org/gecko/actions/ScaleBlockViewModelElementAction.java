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
    private Point2D oldSize;
    private Point2D oldPos;

    ScaleBlockViewModelElementAction(
        EditorViewModel editorViewModel, BlockViewModelElement<?> element, ElementScalerBlock elementScalerBlock,
        Point2D oldPos, Point2D oldSize) {
        this.editorViewModel = editorViewModel;
        this.element = element;
        this.elementScalerBlock = elementScalerBlock;
        this.oldPos = oldPos;
        this.oldSize = oldSize;
    }

    ScaleBlockViewModelElementAction(
        EditorViewModel editorViewModel, BlockViewModelElement<?> element, ElementScalerBlock elementScalerBlock) {
        this.editorViewModel = editorViewModel;
        this.element = element;
        this.elementScalerBlock = elementScalerBlock;
    }

    @Override
    boolean run() throws GeckoException {
        Point2D oldSizeCopy = oldSize;
        Point2D oldPosCopy = oldPos;
        oldSize = element.getSize();
        oldPos = element.getPosition();

        if (oldSizeCopy != null && oldPosCopy != null) {
            element.setSize(oldSizeCopy);
            element.setPosition(oldPosCopy);
        } else {
            elementScalerBlock.getDecoratorTarget()
                .setEdgePoint(elementScalerBlock.getIndex(), elementScalerBlock.getCenter());
        }

        editorViewModel.updateRegions();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createScaleBlockViewModelElementAction(element, elementScalerBlock, oldPos, oldSize);
    }
}
