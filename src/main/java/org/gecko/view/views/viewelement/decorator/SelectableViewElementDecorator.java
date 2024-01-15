package org.gecko.view.views.viewelement.decorator;

import javafx.geometry.Point2D;
import javafx.scene.Node;

import org.gecko.view.views.viewelement.ViewElementVisitor;
import org.gecko.viewmodel.BlockViewModelElement;

public class SelectableViewElementDecorator extends ViewElementDecorator {

    @Override
    public Node drawElement() {
        return null;
    }

    @Override
    public BlockViewModelElement<?> getTarget() {
        return null;
    }

    @Override
    public Point2D getPosition() {
        return null;
    }

    @Override
    public void bindTo(BlockViewModelElement<?> target) {

    }

    @Override
    public void accept(ViewElementVisitor visitor) {

    }
}
