package org.gecko.view.views.viewelement;

import java.util.List;
import javafx.beans.property.Property;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.gecko.viewmodel.PositionableViewModelElement;

public interface ViewElement<T extends PositionableViewModelElement<?>> {
    Node drawElement(); //TODO needed?

    List<Property<Point2D>> getEdgePoints();

    void setEdgePoint(int index, Point2D point);

    default void setSelected(boolean selected) {
    }

    T getTarget();

    Point2D getPosition();

    void accept(ViewElementVisitor visitor);
}
