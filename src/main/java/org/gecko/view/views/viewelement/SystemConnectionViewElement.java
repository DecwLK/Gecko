package org.gecko.view.views.viewelement;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.gecko.viewmodel.PositionableViewModelElement;

public class SystemConnectionViewElement extends ConnectionViewElement {

    @Override
    public Node drawElement() {
        return null;
    }

    @Override
    public PositionableViewModelElement<?> getTarget() {
        return null;
    }

    @Override
    public Point2D getPosition() {
        return null;
    }

    @Override
    public void bindTo(PositionableViewModelElement<?> target) {

    }

    @Override
    public void accept(ViewElementVisitor visitor) {

    }
}