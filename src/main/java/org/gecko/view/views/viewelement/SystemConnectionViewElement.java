package org.gecko.view.views.viewelement;

import javafx.geometry.Point2D;
import javafx.scene.Node;

import org.gecko.viewmodel.SystemConnectionViewModel;

public class SystemConnectionViewElement extends ConnectionViewElement implements ViewElement<SystemConnectionViewModel> {

    @Override
    public Node drawElement() {
        return null;
    }

    @Override
    public SystemConnectionViewModel getTarget() {
        return null;
    }

    @Override
    public Point2D getPosition() {
        return null;
    }

    @Override
    public void bindTo(SystemConnectionViewModel target) {

    }

    @Override
    public void accept(ViewElementVisitor visitor) {

    }
}
