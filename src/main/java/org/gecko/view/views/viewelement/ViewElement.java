package org.gecko.view.views.viewelement;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.gecko.viewmodel.PositionableViewModelElement;

public interface ViewElement<T extends PositionableViewModelElement<?>> {

    Node drawElement(); //TODO needed?

    T getTarget();

    Point2D getPosition();

    void bindTo(T target);

    default void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
    }
}
