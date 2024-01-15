package org.gecko.view.views.viewelement;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import org.gecko.viewmodel.SystemViewModel;

public class SystemViewElement extends Pane implements ViewElement<SystemViewModel> {

    @Override
    public Node drawElement() {
        return null;
    }

    @Override
    public SystemViewModel getTarget() {
        return null;
    }

    @Override
    public Point2D getPosition() {
        return null;
    }

    @Override
    public void bindTo(SystemViewModel target) {

    }

    @Override
    public void accept(ViewElementVisitor visitor) {

    }
}
