package org.gecko.view.views.viewelement;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.gecko.viewmodel.PortViewModel;

public class VariableBlockViewElement extends Pane implements ViewElement<PortViewModel> {

    @Override
    public Node drawElement() {
        return null;
    }

    @Override
    public PortViewModel getTarget() {
        return null;
    }

    @Override
    public Point2D getPosition() {
        return null;
    }

    @Override
    public void bindTo(PortViewModel target) {

    }

    @Override
    public void accept(ViewElementVisitor visitor) {

    }
}
