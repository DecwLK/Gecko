package org.gecko.view.views.viewelement;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.gecko.viewmodel.PositionableViewModelElement;

/**
 * Provides methods used in the visualization of an element in the view. Any such view element corresponds to a
 * view-model element, which is why the interface is generic, encapsulating a type of
 * {@link PositionableViewModelElement}. Concrete visitors must implement this interface to define specific behavior for
 * each view element.
 */
public interface ViewElement<T extends PositionableViewModelElement<?>> {

    /**
     * Draw the element and returns a javafx node representing the element.
     *
     * @return the javafx node
     */
    Node drawElement();

    /**
     * Get the edge points of the element in world coordinates. The edge points represent the bound points of the
     * element.
     * <p>
     * In case of a connection, these points represent the start and end points of the connection.
     * <p>
     * In case of a region, these points represent the bound points of the region.
     *
     * @return the edge points
     */
    ObservableList<Property<Point2D>> getEdgePoints();

    /**
     * Set the edge point at the given index.
     *
     * @param index the index
     * @param point new point
     * @return true if the edge point was set, false if the edge point could not be set due to size limitations
     */
    default boolean setEdgePoint(int index, Point2D point) {
        return true;
    }

    /**
     * Set the selected state of the element.
     *
     * @param selected the selected state
     */
    void setSelected(boolean selected);

    /**
     * Get the selected state of the element.
     *
     * @return the selected state
     */
    boolean isSelected();

    /**
     * Get the target view model of the element.
     *
     * @return the target view model
     */
    T getTarget();

    /**
     * Get the position of the element in world coordinates.
     *
     * @return the position
     */
    Point2D getPosition();

    /**
     * Get the z priority of the element. The z priority is used to determine the order of the elements in the view.
     *
     * @return the z priority
     */
    int getZPriority();

    /**
     * Accept the visitor.
     *
     * @param visitor the visitor
     */
    void accept(ViewElementVisitor visitor);
}
