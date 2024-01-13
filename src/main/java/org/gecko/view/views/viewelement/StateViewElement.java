package org.gecko.view.views.viewelement;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.gecko.viewmodel.StateViewModel;

public class StateViewElement implements ViewElement<StateViewModel> {

    @Override
    public Node drawElement() {
        return null;
    }

    @Override
    public StateViewModel getTarget() {
        return null;
    }

    @Override
    public Point2D getPosition() {
        return null;
    }

    @Override
    public void bindTo(StateViewModel target) {

    }

    @Override
    public void accept(ViewElementVisitor visitor) {

    }
}
