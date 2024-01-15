package org.gecko.view.views.viewelement;

import javafx.geometry.Point2D;
import javafx.scene.Node;

import org.gecko.viewmodel.PositionableViewModelElement;

public interface ViewElement<T extends PositionableViewModelElement<?>> {

    public Node drawElement(); //TODO needed?

    public T getTarget();

    public Point2D getPosition();

    public void bindTo(T target);

    public void accept(ViewElementVisitor visitor);
}
