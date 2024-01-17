package org.gecko.view.views.viewelement;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.gecko.viewmodel.PositionableViewModelElement;

public interface ViewElement<T extends PositionableViewModelElement<?>> {

    Node drawElement(); //TODO needed?

    T getTarget();

    Point2D getPosition();

    void accept(ViewElementVisitor visitor);
}
